package com.example.s3service.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageNotificationEmail {
    private final static String Subject = "Image has been uploaded";
    private ImageMetadataResponse imageMetadataResponse;
    private String urlToDownload;


    @Override
    public String toString() {
        return """
                Image has been uploaded successfully.
                Image Size: %s
                Image Name: %s
                Image Extension: %s
                You can download the image from the following link: %s
                """.formatted(imageMetadataResponse.getSizeInBytes(), imageMetadataResponse.getImageName(), imageMetadataResponse.getFileExtension(), urlToDownload);

    }
}
