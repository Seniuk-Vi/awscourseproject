package com.example.s3service.service.impl;

import com.example.s3service.mapper.ImageMapper;
import com.example.s3service.model.ImageMetadata;
import com.example.s3service.payload.ImageMetadataResponse;
import com.example.s3service.repository.ImageMetadataRepository;
import com.example.s3service.service.ImageMetadataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ImageMetadataServiceImpl implements ImageMetadataService {

    private final ImageMetadataRepository imageMetadataRepository;

    private final ImageMapper imageMapper;

    @Override
    public ImageMetadataResponse getImageMetadata(String imageName) {
        ImageMetadata byName = imageMetadataRepository.findByImageName(imageName);
        return imageMapper.toImageMetadataResponse(byName);
    }

    @Override
    public ImageMetadataResponse saveImageMetadata(ImageMetadata imageMetadata) {
        ImageMetadata save = imageMetadataRepository.save(imageMetadata);
        return imageMapper.toImageMetadataResponse(save);
    }

    @Override
    @Transactional
    public void deleteImageMetadata(String imageMetadata) {
        imageMetadataRepository.deleteByImageName(imageMetadata);
    }

    @Override
    public ImageMetadataResponse getRandonImageMetadata() {
        ImageMetadata random = imageMetadataRepository.findRandom();
        return imageMapper.toImageMetadataResponse(random);
    }
}
