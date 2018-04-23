package com.jeff.fileuploader.service;

import com.jeff.fileuploader.controller.FileUploaderController;
import com.jeff.fileuploader.exception.FileNotFoundException;
import com.jeff.fileuploader.exception.FileUploadFailureException;
import com.jeff.fileuploader.module.MetaData;
import com.jeff.fileuploader.repository.PersistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class UploadingService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

    @Autowired
    private PersistService persistService;

    @Value("upload.folder.path")
    private String filePathPrefix;

    @Transactional
    public List<MetaData> findAll() {
        return persistService.findAll();
    }

    @Transactional
    public MetaData findMetaDataByName(String name) throws FileNotFoundException {
        Optional<MetaData> res = this.findAll().stream().filter(w -> w.getFileName().equals(name)).findAny();
        try {
            return res.get();
        } catch (NoSuchElementException e) {
            logger.error("File with name {} not found.", name);
            throw new FileNotFoundException(name);
        }
    }

    @Transactional(rollbackFor = FileNotFoundException.class)
    public MetaData save(MultipartFile file) {
        MetaData metaData = new MetaData(file.getOriginalFilename());
        metaData.setFileSize((int) file.getSize()).
                setType(file.getContentType()).
                setPath(filePathPrefix);
        try {
            Files.write(Paths.get(filePathPrefix + file.getOriginalFilename()), file.getBytes());
            persistService.save(metaData);
            return metaData;
        } catch (IOException e) {
            logger.error("File with name {} upload request failed.", file.getOriginalFilename());
            throw new FileUploadFailureException(file.getOriginalFilename());
        }
    }
}