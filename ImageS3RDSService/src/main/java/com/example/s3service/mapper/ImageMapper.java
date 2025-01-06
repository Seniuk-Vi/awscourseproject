package com.example.s3service.mapper;

import com.example.s3service.model.ImageMetadata;
import com.example.s3service.payload.ImageMetadataResponse;
import com.example.s3service.payload.ImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageMetadataResponse toImageMetadataResponse(ImageMetadata imageMetadata);
}
