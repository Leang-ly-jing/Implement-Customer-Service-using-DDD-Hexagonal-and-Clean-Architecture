package kh.edu.istad.platform.restapi.exception;

import kh.edu.istad.platform.domain.exception.BusinessDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BusinessGlobalExceptionHandler {

    @ExceptionHandler(BusinessDomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(BusinessDomainException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(WebExchangeBindException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", 400, "errors", errors));
    }
}
