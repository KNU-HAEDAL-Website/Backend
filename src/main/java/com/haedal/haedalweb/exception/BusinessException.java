package com.haedal.haedalweb.exception;

import com.haedal.haedalweb.constants.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;
}
