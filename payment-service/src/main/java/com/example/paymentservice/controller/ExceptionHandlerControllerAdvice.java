package com.example.paymentservice.controller;

import com.example.paymentservice.dto.ErrorDto;
import com.example.paymentservice.exception.BalanceExistsException;
import com.example.paymentservice.exception.BalanceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler({BalanceNotFoundException.class, MethodArgumentNotValidException.class,
            BalanceExistsException.class})
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.badRequest().body(errorDto);
    }
}
