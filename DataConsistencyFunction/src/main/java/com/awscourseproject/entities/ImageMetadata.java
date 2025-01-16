package com.awscourseproject.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="image_metadata")
public class ImageMetadata {

    public ImageMetadata() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "image_name", length = 20, unique = true)
    private String imageName;

    @Column(name = "size_in_bytes")
    private Integer sizeInBytes;

    @Column(name = "file_extension", length = 40)
    private String fileExtension;

    @Column(name = "last_update",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            insertable = false,
            updatable = false)
    private LocalDateTime lastUpdate;

    @Override
    public String toString() {
        return "ImageMetadata{" +
                "id=" + id +
                ", imageName='" + imageName + '\'' +
                ", sizeInBytes=" + sizeInBytes +
                ", fileExtention='" + fileExtension + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Integer sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
