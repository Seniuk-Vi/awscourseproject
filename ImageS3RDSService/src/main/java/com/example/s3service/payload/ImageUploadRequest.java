package com.example.s3service.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadRequest {
    private String imageName;
    private String fileExtention;
    private MultipartFile file;
}
