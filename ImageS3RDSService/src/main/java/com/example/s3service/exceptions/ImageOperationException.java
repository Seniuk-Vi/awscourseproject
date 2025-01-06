package com.example.s3service.exceptions;

public class ImageOperationException extends RuntimeException{

    public ImageOperationException(String message) {
        super(message);
    }

    public ImageOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
