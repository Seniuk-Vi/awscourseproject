package com.example.s3service.payload;

import lombok.Data;

@Data
public class ImageUploadRequest {
    private String imageName;
    private String fileExtention;
    private byte[] file;
}
