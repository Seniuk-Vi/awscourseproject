package com.example.s3service.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

//@Component
@Slf4j
@RequiredArgsConstructor
public class S3BucketInitializer {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket.name}")
    private String bucketName;

    @PostConstruct
    public void createBucketIfNotExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
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