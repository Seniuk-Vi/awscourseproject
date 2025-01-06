package com.example.s3service.service;

import com.example.s3service.model.ImageMetadata;
import com.example.s3service.payload.ImageMetadataResponse;
import org.springframework.stereotype.Service;

@Service
public interface ImageMetadataService {
    ImageMetadataResponse getImageMetadata(String imageName);

    ImageMetadataResponse saveImageMetadata(ImageMetadata imageMetadata);

    void deleteImageMetadata(String imageMetadata);

    ImageMetadataResponse getRandonImageMetadata();


}
