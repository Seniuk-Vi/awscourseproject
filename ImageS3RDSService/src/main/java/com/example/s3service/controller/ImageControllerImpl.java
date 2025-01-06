package com.example.s3service.controller;

import com.example.s3service.api.ImageController;
import com.example.s3service.payload.ImageMetadataResponse;
import com.example.s3service.payload.ImageResponse;
import com.example.s3service.payload.ImageUploadRequest;
import com.example.s3service.service.ImageMetadataService;
import com.example.s3service.service.impl.ImageServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.lang.Boolean.TRUE;

@AllArgsConstructor
public class ImageControllerImpl implements ImageController {

    private final ImageServiceImpl imageService;

    private final ImageMetadataService imageMetadataService;

    @Override
    public ResponseEntity<ImageMetadataResponse> getImageMetadata(String name, Boolean isRandom) {
        if (TRUE.equals(isRandom)) {
            return new ResponseEntity<>(imageMetadataService.getRandonImageMetadata(), HttpStatus.OK);
        }

        return new ResponseEntity<>(imageMetadataService.getImageMetadata(name), HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<ImageResponse> getImage(String name) {
        return new ResponseEntity<>(imageService.downloadImage(name), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ImageResponse> save(ImageUploadRequest imageUploadRequest) {
        return new ResponseEntity<>(imageService.uploadImage(imageUploadRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String name) {
        imageService.deleteImage(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
