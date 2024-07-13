package com.haedal.haedalweb.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode implements ResponseCode{
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "001", "Parameter is invalid."),
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "002", "중복된 아이디가 존재합니다."),
    DUPLICATED_STUDENT_NUMBER(HttpStatus.CONFLICT, "003", "중복된 학번이 존재합니다."),
    INVALID_LOGIN_CONTENTS_TYPE(HttpStatus.BAD_REQUEST, "004", "지원하지 않는 형식입니다."),
    FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "005", "아이디 또는 비밀번호가 일치하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "006", "Access Token has expired."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "007", "Access Token is invalid."),
    NULL_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "008", "Refresh Token is null."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "009", "Refresh Token has expired."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "010", "Refresh Token is invalid."),
    NOT_FOUND_USER_ID(HttpStatus.NOT_FOUND, "011", "유저 아이디를 찾을 수 없습니다."),
    NOT_FOUND_ROLE(HttpStatus.NOT_FOUND, "012", "해당하는 권한을 찾을 수 없습니다."),
    DUPLICATED_SEMESTER(HttpStatus.CONFLICT, "013", "이미 해당 학기가 존재합니다."),
    NOT_FOUND_SEMESTER_ID(HttpStatus.NOT_FOUND, "014", "해당 학기를 찾을 수 없습니다."),
    NOT_FOUND_ACTIVITY_ID(HttpStatus.NOT_FOUND, "015", "해당 활동을 찾을 수 없습니다."),
    EXIST_ACTIVITY(HttpStatus.CONFLICT, "016", "해당 학기에 활동이 존재하는 경우 삭제할 수 없습니다."),
    EXIST_BOARD(HttpStatus.CONFLICT, "017", "해당 활동에 게시판이 존재하는 경우 삭제할 수 없습니다."),
    NOT_FOUND_BOARD_ID(HttpStatus.NOT_FOUND, "018", "해당 게시판을 찾을 수 없습니다."),
    FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "019", "삭제 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
