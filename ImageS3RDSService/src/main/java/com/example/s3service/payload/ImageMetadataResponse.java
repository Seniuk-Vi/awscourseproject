package com.example.s3service.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageMetadataResponse {
    private String imageName;
    private Integer sizeInBytes;
    private String fileExtension;
    private LocalDateTime lastUpdate;
}
