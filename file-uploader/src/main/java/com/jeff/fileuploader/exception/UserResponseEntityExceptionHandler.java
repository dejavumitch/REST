package com.jeff.fileuploader.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@Component
@ControllerAdvice
@RestController
public class UserResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest wr) {
        ExceptionResponse exr =
                new ExceptionResponse(new Date(), ex.getMessage(), wr.getDescription(false));
        return new ResponseEntity<>(exr, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public final ResponseEntity<Object> handleFileNotFoundExceptions(FileNotFoundException ex, WebRequest wr) {
        ExceptionResponse exr =
                new ExceptionResponse(new Date(), ex.getMessage(), wr.getDescription(false));
        return new ResponseEntity<>(exr, NOT_FOUND);
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    public final ResponseEntity<Object> handleFileAlreadyExistsExceptions(FileAlreadyExistsException ex, WebRequest wr) {
        ExceptionResponse exr =
                new ExceptionResponse(new Date(), ex.getMessage(), wr.getDescription(false));
        return new ResponseEntity<>(exr, CONFLICT);
    }

    @ExceptionHandler(FileUploadFailureException.class)
    public final ResponseEntity<Object> handleFileUploadFailureExceptions(FileUploadFailureException ex, WebRequest wr) {
        ExceptionResponse exr =
                new ExceptionResponse(new Date(), ex.getMessage(), wr.getDescription(false));
        return new ResponseEntity<>(exr, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileDownloadFailureException.class)
    public final ResponseEntity<Object> handleFileDownloadFailureExceptions(FileDownloadFailureException ex, WebRequest wr) {
        ExceptionResponse exr =
                new ExceptionResponse(new Date(), ex.getMessage(), wr.getDescription(false));
        return new ResponseEntity<>(exr, INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest req) {
        ExceptionResponse exr =
                new ExceptionResponse(new Date(), "Validation Failed", ex.getBindingResult().toString());
        return new ResponseEntity<>(exr, BAD_REQUEST);
    }


}
