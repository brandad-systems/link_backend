package io.example.service;

import io.example.domain.dto.ImageView;
import io.example.domain.model.User;
import io.example.utils.Utils;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class ImageService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    public ImageView uploadFile(String name, byte[] content) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        User user = Utils.getUser();
        String fileDir = user.getId().toString();

        long unixTime = System.currentTimeMillis();
        String fileName = unixTime + "-" + name;
        String imagePath = fileDir + "/" + fileName;

        ByteArrayInputStream bais = new ByteArrayInputStream(content);

        try {

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(defaultBucketName).object(imagePath).stream(
                            bais, bais.available(), -1
                    ).build()
            );
            bais.close();
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
        return new ImageView(defaultBucketName + "/" + imagePath);
    }
}
