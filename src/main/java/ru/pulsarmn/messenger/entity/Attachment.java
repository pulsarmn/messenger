package ru.pulsarmn.messenger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;


@Embeddable
public class Attachment {

    @Column(name = "file_key")
    private String fileKey;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "duration")
    private Integer duration;

    public Attachment() {
    }

    public Attachment(String fileKey, String fileName, Long fileSize, String mimeType, Integer width, Integer height, Integer duration) {
        this.fileKey = fileKey;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.width = width;
        this.height = height;
        this.duration = duration;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Attachment that = (Attachment) object;
        return Objects.equals(fileKey, that.fileKey) && Objects.equals(fileName, that.fileName) && Objects.equals(fileSize, that.fileSize) && Objects.equals(mimeType, that.mimeType) && Objects.equals(width, that.width) && Objects.equals(height, that.height) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileKey, fileName, fileSize, mimeType, width, height, duration);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "fileKey='" + fileKey + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", mimeType='" + mimeType + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", duration=" + duration +
                '}';
    }
}
