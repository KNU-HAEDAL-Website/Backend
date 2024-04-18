package com.haedal.haedalweb.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "중복된 아이디가 존재합니다."),
    DUPLICATED_STUDENT_NUMBER(HttpStatus.CONFLICT, "중복된 학번이 존재합니다."),
    INVALID_LOGIN_CONTENTS_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 형식입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
