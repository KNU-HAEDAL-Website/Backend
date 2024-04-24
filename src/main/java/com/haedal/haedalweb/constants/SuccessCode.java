package com.haedal.haedalweb.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SuccessCode {
    JOIN_SUCCESS(HttpStatus.CREATED, true, "회원가입을 축하드립니다."),
    UNIQUE_USER_ID(HttpStatus.OK, true, "사용 가능한 ID입니다."),
    DUPLICATED_USER_ID(HttpStatus.OK, false, "중복된 ID입니다. 다른 ID를 입력해 주세요."),
    UNIQUE_STUDENT_NUMBER(HttpStatus.OK, true, "사용 가능한 학번입니다."),
    DUPLICATED_STUDENT_NUMBER(HttpStatus.OK, false, "중복된 학번입니다. 다시 확인해 주세요."),
    LOGIN_SUCCESS(HttpStatus.OK, true, "로그인에 성공했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, true, "로그아웃에 성공했습니다."),
    REISSUE_SUCCESS(HttpStatus.OK, true, "토큰을 재발급했습니다.");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
