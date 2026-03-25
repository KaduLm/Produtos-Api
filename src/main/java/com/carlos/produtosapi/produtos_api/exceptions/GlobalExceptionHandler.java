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



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ProdutoNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(getResponseErro(
                        HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.name(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenGeneration(TokenGenerationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getResponseErro(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenValidation(TokenValidationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(getResponseErro(
                        HttpStatus.UNAUTHORIZED,
                        HttpStatus.UNAUTHORIZED.name(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(UsuarioAlredyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseErro(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        boolean hasMissingRequiredFields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .anyMatch(error -> error.getDefaultMessage() != null &&
                        error.getDefaultMessage().toLowerCase().contains("obrigatório"));

        HttpStatus status = hasMissingRequiredFields
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.UNPROCESSABLE_ENTITY;

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Erro de validação");

        return ResponseEntity.status(status)
                .body(getResponseErro(
                        status,
                        "Validation Error",
                        message
                ));
    }


    private static ErrorResponseDTO getResponseErro(HttpStatus httpStatus, String tipoError, String messagemError) {
        return ErrorResponseDTO.builder()
                .error(tipoError)
                .status(httpStatus.value())
                .message(messagemError)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
