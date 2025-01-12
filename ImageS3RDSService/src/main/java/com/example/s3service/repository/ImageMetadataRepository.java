package com.example.s3service.repository;

import com.example.s3service.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {

    ImageMetadata findByImageName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM image_metadata ORDER BY RANDOM() LIMIT 1;")
    ImageMetadata findRandom();

    void deleteByImageName(String name);
}
