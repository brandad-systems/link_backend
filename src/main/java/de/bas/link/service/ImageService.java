package de.bas.link.service;

import de.bas.link.domain.dto.ImageView;
import de.bas.link.domain.model.User;
import de.bas.link.utils.Utils;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class ImageService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    public ImageView uploadFile(String name, byte[] content) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if(!Utils.isImageStream(content)) {
            log.error(String.format("File '%s' is not an image!", name));
            throw new ValidationException("Filetype is not an image.");
        }

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
