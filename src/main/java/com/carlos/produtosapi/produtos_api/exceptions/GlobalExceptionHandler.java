package com.carlos.produtosapi.produtos_api.exceptions;

import com.carlos.produtosapi.produtos_api.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ProdutoNotFoundException ex, HttpServletRequest request) {
        var apiResponse = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
