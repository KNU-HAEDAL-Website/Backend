package com.haedal.haedalweb.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "중복된 아이디가 존재합니다."),
    DUPLICATED_STUDENT_NUMBER(HttpStatus.CONFLICT, "중복된 학번이 존재합니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
