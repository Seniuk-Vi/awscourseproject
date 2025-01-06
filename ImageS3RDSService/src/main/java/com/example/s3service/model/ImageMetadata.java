package com.example.s3service.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="image_metadata")
@Data
@Builder
public class ImageMetadata {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "image_name", length = 20, unique = true)
    private String imageName;

    @Column(name = "size_in_bytes")
    private Integer sizeInBytes;

    @Column(name = "file_extention", length = 40)
    private String fileExtention;

    @Column(name = "last_update",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
            insertable = false,
            updatable = false)
    private LocalDateTime lastUpdate;

    @Override
    public String toString() {
        return "ImageMetadata{" +
                "id=" + id +
                ", imageName='" + imageName + '\'' +
                ", sizeInBytes=" + sizeInBytes +
                ", fileExtention='" + fileExtention + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
