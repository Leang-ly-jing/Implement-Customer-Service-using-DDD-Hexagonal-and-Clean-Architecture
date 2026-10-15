package kh.edu.istad.platform.order.restapi.exception;

import kh.edu.istad.platform.order.domain.exception.OrderDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class OrderGlobalExceptionHandler {

    @ExceptionHandler(OrderDomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(OrderDomainException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
    }
}
