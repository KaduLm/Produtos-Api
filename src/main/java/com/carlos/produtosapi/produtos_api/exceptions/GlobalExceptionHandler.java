package com.carlos.produtosapi.produtos_api.exceptions;

import com.carlos.produtosapi.produtos_api.dto.response.ErrorResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ProdutoNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenGeneration(TokenGenerationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenValidation(TokenValidationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado",
                LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
