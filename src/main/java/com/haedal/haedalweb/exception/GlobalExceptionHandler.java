package com.haedal.haedalweb.exception;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
}
