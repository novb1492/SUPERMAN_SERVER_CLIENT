package com.kimcompany.jangbogbackendver2.Aws;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    public String uploadImage(File file, String bucketName) {
        try {
            String imgName = file.getName();
            amazonS3.putObject(bucketName, imgName, file);
            return imgName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFile(String bucktetName,String fileName) {
        amazonS3.deleteObject(bucktetName, fileName);
    }
}
