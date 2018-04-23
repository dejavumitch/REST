package com.jeff.fileuploader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super("File with name " + message + " not found");
    }

    public FileNotFoundException() {
        super("No files found.");
    }
}
