package com.jeff.fileuploader.module;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class MetaData {
    @Id
    @GeneratedValue
    private Integer fileId;

    @NotNull
    private String fileName;

    @Positive
    private int fileSize;

    @NotBlank
    private String path;

    @NotNull
    private String type;

    protected MetaData() {

    }

    public MetaData(@NotBlank String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public MetaData setPath(String path) {
        this.path = path;
        return this;
    }

    public int getFileSize() {
        return fileSize;
    }

    public MetaData setFileSize(int fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getType() {
        return type;

    }

    public MetaData setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public void setId(int id) {
        this.fileId = id;
    }
}
