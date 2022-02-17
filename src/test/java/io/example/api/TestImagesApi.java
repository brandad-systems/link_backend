package io.example.api;


import io.example.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


@WebMvcTest(controllers = {ImagesApi.class})
public class TestImagesApi {

    @Autowired
    private ImageService imageService;

    @Test
    public void testAddImageToMinio_return200()
    {

    }

}
