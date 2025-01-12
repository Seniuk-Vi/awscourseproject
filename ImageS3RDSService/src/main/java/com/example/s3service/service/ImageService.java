package com.example.s3service.service;

import com.example.s3service.payload.ImageResponse;
import com.example.s3service.payload.ImageUploadRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ImageService {
    ImageResponse uploadImage(ImageUploadRequest imageUploadRequest) throws IOException;

    ImageResponse downloadImage(String imageName);

    void deleteImage(String imageName);
}
