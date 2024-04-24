package com.haedal.haedalweb.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "중복된 아이디가 존재합니다."),
    DUPLICATED_STUDENT_NUMBER(HttpStatus.CONFLICT, "중복된 학번이 존재합니다."),
    INVALID_LOGIN_CONTENTS_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 형식입니다."),
    FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token has expired."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token is invalid."),
    NULL_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token is null."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token has expired."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token is invalid."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Parameter is invalid.");

    private final HttpStatus httpStatus;
    private final String message;
}
