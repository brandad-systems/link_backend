package de.bas.link.service;

import de.bas.link.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    private ImageService imageService;

    @BeforeEach
    public void setUp()
    {
        imageService = new ImageService();
    }

    @Test()
    public void uploadedImageIsNotValidImage() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String fileName = "image.jpg";
        String hexString = "504B030414";

        try {
            imageService.uploadFile(fileName, Utils.hexStringToByteArray(hexString));
        } catch (ValidationException e) {
            assertThat(e.getMessage()).isEqualTo("Filetype is not an image.");
        }
    }
}
