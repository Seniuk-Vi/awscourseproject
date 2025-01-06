package com.example.s3service.service.impl;

import com.example.s3service.exceptions.ImageOperationException;
import com.example.s3service.model.ImageMetadata;
import com.example.s3service.payload.ImageMetadataResponse;
import com.example.s3service.payload.ImageResponse;
import com.example.s3service.payload.ImageUploadRequest;
import com.example.s3service.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Client s3Client;

    private static final String FILE_EXTENSION = ".jpg";

    private final ImageMetadataServiceImpl imageMetadataService;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public ImageResponse uploadImage(ImageUploadRequest imageUploadRequest) {
        if (imageMetadataService.getImageMetadata(imageUploadRequest.getImageName()) != null) {
            throw new ImageOperationException("Image with name " + imageUploadRequest.getImageName() + " already exists");
        }

        PutObjectResponse putObjectResponse = s3Client.putObject(
                request ->
                        request.bucket(bucketName)
                                .key(imageUploadRequest.getImageName().concat(FILE_EXTENSION))
                                .build(),
                RequestBody.fromBytes(imageUploadRequest.getFile())
        );

        if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
            ImageMetadataResponse imageMetadataResponse = imageMetadataService.saveImageMetadata(ImageMetadata.builder()
                            .fileExtention(FILE_EXTENSION)
                            .imageName(imageUploadRequest.getImageName())
                            .sizeInBytes(imageUploadRequest.getFile().length)
                            .build());
            return ImageResponse.builder()
                    .metadata(imageMetadataResponse)
                    .file(imageUploadRequest.getFile())
                    .build();
        }

        throw new ImageOperationException("Failed to upload image: " +
                putObjectResponse.sdkHttpResponse().statusText().orElse("Unknown error"));
    }

    @Override
    public ImageResponse downloadImage(String imageName) {
        if (imageMetadataService.getImageMetadata(imageName) == null) {
            throw new ImageOperationException("Image with name " + imageName + " does not exist");
        }

        ResponseBytes<GetObjectResponse> object = s3Client.getObject(
                request -> request.bucket(bucketName).key(imageName),
                ResponseTransformer.toBytes()
        );

        if (object.asByteArray().length == 0) {
            throw new ImageOperationException("Failed to download image: " + imageName);
        }

        ImageMetadataResponse imageMetadata = imageMetadataService.getImageMetadata(imageName);

        return ImageResponse.builder()
                .file(object.asByteArray())
                .metadata(imageMetadata)
                .build();
    }

    @Override
    public void deleteImage(String imageName) {
        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(
                request -> request.bucket(bucketName).key(imageName)
        );

        if (deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
            imageMetadataService.deleteImageMetadata(imageName);
        }

        throw new ImageOperationException("Failed to delete image: " +
                deleteObjectResponse.sdkHttpResponse().statusText().orElse("Unknown error"));
    }
}
