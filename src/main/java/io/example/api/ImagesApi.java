package io.example.api;

import io.example.domain.dto.ImageView;
import io.example.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/image")

@Slf4j
public class ImagesApi {

    @Autowired
    private ImageService imageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImageView> uploadFile(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        return new ResponseEntity<>(imageService.uploadFile(file.getOriginalFilename(), file.getBytes()), HttpStatus.CREATED);
    }

    //TODO: GET Images genauer definieren (Wie werden Images referenziert, welche Images werden gebraucht, ...)
    @GetMapping("/{folderName}/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String folderName, @PathVariable String fileName) {
        byte[] bytes = imageService.downloadFile(folderName, fileName);
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

    @GetMapping("/toDisk")
    public void downloadFileToDisk() {
        imageService.downloadFileToDisk();
    }
}
