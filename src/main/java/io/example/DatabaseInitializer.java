package io.example;

import io.example.domain.dto.CreateUserRequest;
import io.example.domain.model.Role;
import io.example.service.UserService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<String> usernames = List.of(
            "ada.lovelace@nix.io",
            "alan.turing@nix.io",
            "dennis.ritchie@nix.io"
    );
    private final List<String> fullNames = List.of(
            "Ada Lovelace",
            "Alan Turing",
            "Dennis Ritchie"
    );
    private final List<String> roles = List.of(
            Role.USER_ADMIN,
            Role.AUTHOR_ADMIN,
            Role.BOOK_ADMIN
    );
    private final String password = "Test12345_";

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    private final UserService userService;

    private final MinioClient minioClient;

    public DatabaseInitializer(UserService userService, MinioClient minioClient) {
        this.userService = userService;
        this.minioClient = minioClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        for (int i = 0; i < usernames.size(); ++i) {
            CreateUserRequest request = new CreateUserRequest();
            request.setUsername(usernames.get(i));
            request.setFullName(fullNames.get(i));
            request.setPassword(password);
            request.setRePassword(password);
            request.setAuthorities(Set.of(roles.get(i)));

            userService.upsert(request);
        }

        // Check if S3 Bucket 'images' exists
        try {
            boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(defaultBucketName).build());
            if (found) {
                log.info(String.format("Default bucket '%s' exists.", defaultBucketName));
            } else {
                log.warn(String.format("Default bucket '%s' does not exists. Creating new one...", defaultBucketName));
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(defaultBucketName).build());
                log.info(String.format("Bucket '%s' successfully created.", defaultBucketName));
            }
        } catch (MinioException e) {
            log.error("Error occurred: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

}
