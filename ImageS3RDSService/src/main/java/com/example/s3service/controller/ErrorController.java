package com.example.s3service.controller;

import com.example.s3service.exceptions.ImageOperationException;
import com.example.s3service.payload.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;


import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorController {

    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(ImageOperationException.class)
    public ResponseEntity<ErrorDto> handleImageOperationException(HttpServletRequest request) {
        log.error("Image operation exception occurred", request);
        return ResponseEntity.status(BAD_REQUEST)
                .body(buildErrorDto(request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(HttpServletRequest request) {
        log.error("Internal server error occurred", request);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(buildErrorDto(request));
    }

    private ErrorDto buildErrorDto(HttpServletRequest request) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Throwable error = errorAttributes.getError(servletWebRequest);
        String message = getErrorMessage(error);
        return ErrorDto.builder()
                .message(message)
                .build();
    }

    private String getErrorMessage(Throwable e) {
        String message = null;
        if (e != null) {
            message = e.getMessage();
            message = !StringUtils.hasLength(message)
                    && e.getCause() != null && !StringUtils.hasLength(e.getCause().getMessage()) ? e
                    .getCause()
                    .getMessage() : message;
        }
        return message;
    }
}
