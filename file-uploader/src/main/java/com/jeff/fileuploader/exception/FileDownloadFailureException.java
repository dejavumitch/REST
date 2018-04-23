package com.jeff.fileuploader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileDownloadFailureException extends RuntimeException {
    public FileDownloadFailureException(String message) {
        super("Unable to download file with name " + message);
    }
}