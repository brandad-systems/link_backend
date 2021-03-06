package de.bas.link.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.bas.link.configuration.security.JwtTokenFilter;
import de.bas.link.configuration.security.SecurityConfig;
import de.bas.link.domain.dto.ImageView;
import de.bas.link.service.ImageService;
import de.bas.link.utils.Utils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.validation.ValidationException;
import java.util.List;

import static de.bas.link.util.JsonHelper.fromJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
//@WithUserDetails("ada.lovelace@nix.io")
// : nur halbe Laufzeit, weil nur wenige Spring - Componenten initialisiert werden,
// : aber: keine Security (die muss dann an anderer Stelle getestet werden)
@WebMvcTest(controllers = ImagesApi.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtTokenFilter.class}) })

@AutoConfigureMockMvc(addFilters = false)
//@TestPropertySource(properties = { "myprop=myval", "myprop2=myval2" })
//@Import({ /* only of  contrller depends on a class that we don't want to mock:  MyClazz.class */ })
public class TestImagesApi {

    @Autowired
    private MockMvc mockMvc;

    @Value("${minio.url}")
    String minioUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ImageService imageService;

    @Test
    public void testAddImageToMinio_returnCreated() throws Exception {

        when(imageService.uploadFile("image.jpg", Utils.hexStringToByteArray("FFD8FFE000104A"))).thenReturn(new ImageView("123/234-image.jpg"));

        MockMultipartFile file
                = new MockMultipartFile("file", "image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, Utils.hexStringToByteArray("FFD8FFE000104A"));

        MvcResult uploadResult = mockMvc.perform(multipart("/api/v1/image").file(file))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", minioUrl + "/" + "123/234-image.jpg"))
                .andReturn();

        ImageView imageView = fromJson(objectMapper, uploadResult.getResponse().getContentAsString(), ImageView.class);
    }

    @Test
    public void invalidFileIsRejected() throws Exception {
        String fileName = "file.zip";
        byte[] fileContent = Utils.hexStringToByteArray("504B030414");
        when(imageService.uploadFile(fileName, fileContent)).thenThrow(new ValidationException("Filetype is not an image."));

        MockMultipartFile file
                = new MockMultipartFile("file", fileName, MediaType.MULTIPART_FORM_DATA_VALUE, fileContent);

        mockMvc.perform(multipart("/api/v1/image").file(file))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Disabled
    public void testAddMultipleImagesToMinio_returnCreated() throws Exception {
        MockMultipartFile file1
                = new MockMultipartFile("file", "image.jpg", MediaType.APPLICATION_OCTET_STREAM_VALUE, "image.jpg".getBytes());
        MockMultipartFile file2
                = new MockMultipartFile("file", "image1.jpg", MediaType.APPLICATION_OCTET_STREAM_VALUE, "image1.jpg".getBytes());

        mockMvc.perform(multipart("/api/v1/image").file(file1).file(file2))
                .andExpect(status().isCreated());

        ArgumentCaptor<String> originalFileNameCaptor = ArgumentCaptor.forClass(String.class);
        // ArgumentCaptor<String> byteArrayCaptor = ArgumentCaptor.forClass(Byte);
        verify(imageService, times(2)).uploadFile(originalFileNameCaptor.capture(), any());
        // verify(mock, times(2)).doSomething(peopleCaptor.capture());

        List<String> originalFileName = originalFileNameCaptor.getAllValues();
        assertThat("image.jpg").isEqualTo(originalFileName.get(0));
        assertThat("image1.jpg").isEqualTo(originalFileName.get(1));
    }

}
