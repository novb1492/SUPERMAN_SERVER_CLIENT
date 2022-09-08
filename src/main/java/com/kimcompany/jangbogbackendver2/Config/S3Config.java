package com.kimcompany.jangbogbackendver2.Config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kimcompany.jangbogbackendver2.Text.PropertiesText.awsAccessKey;
import static com.kimcompany.jangbogbackendver2.Text.PropertiesText.awsSecret;

@Configuration
public class S3Config {
    @Bean
    public AmazonS3 S3Client() {
        AWSCredentials credentials=new BasicAWSCredentials(awsAccessKey, awsSecret);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_NORTHEAST_2).build();
    }
}
