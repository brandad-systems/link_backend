package io.example.service;

import io.example.domain.model.User;
import io.example.utils.Utils;
import io.minio.*;
import io.minio.errors.MinioException;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    public void uploadFile(String name, byte[] content) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        User user = Utils.getUser();
        String fileDir = user.getId().toString();

        long unixTime = System.currentTimeMillis();
        String fileName = unixTime + "-" + name;

        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(defaultBucketName).object( fileDir + "/" + fileName).stream(
                            bais, bais.available(), -1
                    ).build()
            );
            bais.close();
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }

    public void downloadFileToDisk() {

        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object("folder/image2.jpg")
                            .filename("my-object-file.jpg")
                            .build());
        }
        catch(MinioException e)
        {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    // filename is a combination of userid + data name and
    // is stored in database as picture ids against a user
    public byte[] downloadFile(String folderName, String fileName)  {

        byte[]  bytes = new byte[] {};
        try {
            InputStream stream = minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(defaultBucketName)
                        .object(folderName + "/" + fileName)
                        .build());
            bytes = IOUtils.toByteArray(stream);
        }
        catch(MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
