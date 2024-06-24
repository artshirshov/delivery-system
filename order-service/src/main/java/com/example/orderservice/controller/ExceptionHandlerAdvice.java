package com.example.orderservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.orderservice.dto.ErrorDto;
import com.example.orderservice.exception.OrderNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({OrderNotFoundException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorMessage(ex.getMessage());
        errorDto.setTimestamp(LocalDateTime.now());

        return ResponseEntity.badRequest().body(errorDto);
    }
}
