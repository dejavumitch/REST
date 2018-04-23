package com.jeff.fileuploader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException(String message) {
        super("Unable to create. A File with name " +
                message + " already exist.");
    }
}