package com.anhub.prize_draw_system.config;

import com.anhub.prize_draw_system.draw.exceptions.IncorrectPromoCode;
import com.anhub.prize_draw_system.promocodes.exceptions.AlreadyActivatedPromoCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectPromoCode.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPromoCode(IncorrectPromoCode ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(AlreadyActivatedPromoCode.class)
    public ResponseEntity<ErrorResponse> handleAlreadyActivatedPromoCode(AlreadyActivatedPromoCode ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
