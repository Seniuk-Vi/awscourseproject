package com.example.s3service.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponse {
    private ImageMetadataResponse metadata;
    private byte[] file;
}
