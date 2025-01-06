package com.example.s3service.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Configuration
@Slf4j
@AllArgsConstructor
public class S3Configuration {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.bucket.region}")
    private String bucketRegion;


    private final S3Client s3Client;

    @Bean
    public S3Client amazonS3() {
        return S3Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(bucketRegion))
                .build();
    }


    @PostConstruct
    public void createBucketIfNotExists() {
        try {
            s3Client.headBucket(request -> request.bucket(bucketName).build());
            log.info("Bucket already exists: " + bucketName);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                s3Client.createBucket(request -> request.bucket(bucketName));
                log.info("Bucket created: " + bucketName);
            } else {
                log.error("Error checking if bucket exists: " + e.awsErrorDetails().errorMessage());
                throw e;
            }
        }
    }
}
