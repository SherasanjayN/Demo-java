package com.farmstock.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<Map<String, Object>> body(HttpStatus s, Object msg) {
        return ResponseEntity.status(s).body(Map.of("timestamp", Instant.now().toString(),
                "status", s.value(), "error", s.getReasonPhrase(), "message", msg));
    }
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<?> nf(NotFoundException e) { return body(HttpStatus.NOT_FOUND, e.getMessage()); }
    @ExceptionHandler(InsufficientStockException.class)
    ResponseEntity<?> stock(InsufficientStockException e) { return body(HttpStatus.CONFLICT, e.getMessage()); }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> bad(IllegalArgumentException e) { return body(HttpStatus.BAD_REQUEST, e.getMessage()); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> valid(MethodArgumentNotValidException e) {
        return body(HttpStatus.BAD_REQUEST, e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(f -> f.getField(), f -> String.valueOf(f.getDefaultMessage()), (a, b) -> a)));
    }
}
