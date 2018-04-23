package com.jeff.fileuploader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(String message) {
        super("Unable to upload file with name " + message);
    }
}