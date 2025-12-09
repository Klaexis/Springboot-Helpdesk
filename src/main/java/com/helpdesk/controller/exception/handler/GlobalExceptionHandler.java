package com.helpdesk.controller.exception.handler;

import com.helpdesk.controller.exception.EmptyPageException;
import com.helpdesk.model.response.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyPageException.class)
    public ResponseEntity<PageResponse<?>> handleEmptyPageException(EmptyPageException ex) {

        return ResponseEntity.ok(
                new PageResponse<>(
                    ex.getMessage(),
                    new Object[0]  // return empty array or empty list
                )
        );
    }
}
