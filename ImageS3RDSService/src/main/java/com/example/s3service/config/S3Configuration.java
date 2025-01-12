package com.example.s3service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class S3Configuration {

    @Value("${spring.cloud.aws.s3.bucket.region}")
    private String bucketRegion;

    @Bean
    public S3Client amazonS3() {
        return S3Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(bucketRegion))
                .build();
    }

}
