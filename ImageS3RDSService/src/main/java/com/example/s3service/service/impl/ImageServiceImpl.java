package com.example.s3service.service.impl;

import com.example.s3service.controller.ImageControllerImpl;
import com.example.s3service.exceptions.ImageOperationException;
import com.example.s3service.model.ImageMetadata;
import com.example.s3service.payload.ImageMetadataResponse;
import com.example.s3service.payload.ImageNotificationEmail;
import com.example.s3service.payload.ImageResponse;
import com.example.s3service.payload.ImageUploadRequest;
import com.example.s3service.service.ImageMetadataService;
import com.example.s3service.service.ImageService;
import com.example.s3service.service.NotificationService;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static org.apache.commons.lang3.StringUtils.joinWith;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final S3Client s3Client;

    private static final String FILE_EXTENSION = ".jpg";

    private final NotificationService notificationService;

    private final ImageMetadataService imageMetadataService;

    @Value("${spring.cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public ImageResponse uploadImage(ImageUploadRequest imageUploadRequest) throws IOException {
        log.info("Uploading image: {}", imageUploadRequest.getImageName());
        if (imageMetadataService.getImageMetadata(imageUploadRequest.getImageName()) != null) {
            throw new ImageOperationException("Image with name " + imageUploadRequest.getImageName() + " already exists");
        }

        PutObjectResponse putObjectResponse = s3Client.putObject(
                request ->
                        request.bucket(bucketName)
                                .key(imageUploadRequest.getImageName().concat(FILE_EXTENSION))
                                .build(),
                RequestBody.fromBytes(imageUploadRequest.getFile().getBytes())
        );

        if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
            ImageMetadataResponse imageMetadataResponse = imageMetadataService.saveImageMetadata(ImageMetadata.builder()
                            .fileExtension(FILE_EXTENSION)
                            .imageName(imageUploadRequest.getImageName())
                            .sizeInBytes(imageUploadRequest.getFile().getBytes().length)
                            .build());

            String sqsMessage = createMessage(imageMetadataResponse);
            notificationService.sendSqsMessage(sqsMessage);
            return ImageResponse.builder()
                    .metadata(imageMetadataResponse)
                    .file(imageUploadRequest.getFile().getBytes())
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
        imageMetadataService.deleteImageMetadata(imageName);

        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(
                request -> request.bucket(bucketName).key(imageName)
        );

        if (!deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
            throw new ImageOperationException("Failed to delete image: " +
                    deleteObjectResponse.sdkHttpResponse().statusText().orElse("Unknown error"));
        }
    }

    private String createMessage(ImageMetadataResponse imageMetadataResponse) {
        String downloadLink = linkTo(methodOn(ImageControllerImpl.class).getImage(imageMetadataResponse.getImageName())).toString();
        ImageNotificationEmail imageNotificationEmail = new ImageNotificationEmail(imageMetadataResponse, downloadLink);
        return imageNotificationEmail.toString();
    }

}
