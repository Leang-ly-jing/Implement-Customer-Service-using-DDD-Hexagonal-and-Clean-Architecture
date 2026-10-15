package kh.edu.istad.platform.payment.restapi.exception;

import kh.edu.istad.platform.domain.exception.PaymentDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class PaymentGlobalExceptionHandler {

    @ExceptionHandler(PaymentDomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(PaymentDomainException exception) {
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
