package de.bas.link.api;

import de.bas.link.domain.dto.ImageView;
import de.bas.link.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/image")
public class ImagesApi {

    @Value("${minio.url}")
    String minioUrl;

    @Autowired
    private ImageService imageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImageView> uploadFile(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        String fileName = ((file.getOriginalFilename() == null) ? "created.jpg" : file.getOriginalFilename());
        ImageView createdImage = imageService.uploadFile(fileName, file.getBytes());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", minioUrl + "/" + createdImage.getImagePath());
        return new ResponseEntity<>(createdImage, responseHeaders, HttpStatus.CREATED);
    }
}
