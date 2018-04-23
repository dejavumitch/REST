package com.jeff.fileuploader.controller;

import com.jeff.fileuploader.exception.FileAlreadyExistsException;
import com.jeff.fileuploader.exception.FileDownloadFailureException;
import com.jeff.fileuploader.exception.FileNotFoundException;
import com.jeff.fileuploader.module.MetaData;
import com.jeff.fileuploader.service.UploadingService;
import com.sun.net.httpserver.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@RestController
@RequestMapping("/api")
public class FileUploaderController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

    @Value("io.file.mimeType")
    private static String mimeType;

    @Autowired
    private UploadingService uploadingService; //Service which will do all data retrieval/manipulation work

    // -------------------Retrieve All MetaData---------------------------------------------

    @GetMapping("/file")
    public ResponseEntity<List<MetaData>> listAllFiles() {
        List<MetaData> files = uploadingService.findAll();
        if (files.isEmpty()) {
            throw new FileNotFoundException();
        }
        return new ResponseEntity<List<MetaData>>(files, HttpStatus.OK);
    }

    // -------------------Retrieve Single MetaData------------------------------------------

    @GetMapping("/file/{fileName}")
    public ResponseEntity<MetaData> getMetaData(@PathVariable("fileName") String name) throws FileNotFoundException {
        logger.info("Fetching file by name: {}", name);
        MetaData file = uploadingService.findMetaDataByName(prepossess(name));
        return new ResponseEntity<MetaData>(file, HttpStatus.OK);
    }

    // -------------------download Single File------------------------------------------

    @GetMapping("/file/{fileName}/download")
    public void downloadFile(HttpServletResponse response, @PathVariable("fileName") String name) throws FileNotFoundException {
        logger.info("Fetching file by name: {}", name);
        MetaData metaData = uploadingService.findMetaDataByName(prepossess(name));
        File file = new File(metaData.getPath() + name);
        response.setContentType(mimeType);
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            logger.error("File with name {} download request failed.", name);
            throw new FileDownloadFailureException(name);
        }
    }

    // -------------------Upload a File-------------------------------------------

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            logger.info("Bad input, try again.");
            return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
        }
        try {
            uploadingService.findMetaDataByName(file.getOriginalFilename());
            logger.error("Unable to create. A File with name {} already exist", file.getOriginalFilename());
            throw new FileAlreadyExistsException(file.getOriginalFilename());
        } catch (FileNotFoundException e) {
            uploadingService.save(file);
            logger.info("Creating File : {}", file.getOriginalFilename());
            return new ResponseEntity<Authenticator.Success>(HttpStatus.CREATED);
        }
    }

    // -------------------pre-process filename-------------------------------
    // make file format name case insensitive
    private String prepossess(String name) {
        String[] filename = name.split("\\.");
        StringBuilder sb = new StringBuilder(filename[0]);
        if (filename.length == 2) {
            sb.append(".");
            sb.append(filename[1].toLowerCase());
        }
        return sb.toString();
    }
}
