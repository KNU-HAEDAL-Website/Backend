package com.haedal.haedalweb.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SuccessCode implements ResponseCode{
    JOIN_SUCCESS(HttpStatus.CREATED, true, "회원가입을 축하드립니다."),
    JOIN_APPROVAL(HttpStatus.OK, true, "회원가입을 승인했습니다."),
    JOIN_REFUSAL(HttpStatus.OK, true, "회원가입을 거절했습니다."),
    EXPEL_USER(HttpStatus.OK, true, "회원을 내보냈습니다."),
    UNIQUE_USER_ID(HttpStatus.OK, true, "사용 가능한 ID입니다."),
    DUPLICATED_USER_ID(HttpStatus.OK, false, "중복된 ID입니다. 다른 ID를 입력해 주세요."),
    UNIQUE_STUDENT_NUMBER(HttpStatus.OK, true, "사용 가능한 학번입니다."),
    DUPLICATED_STUDENT_NUMBER(HttpStatus.OK, false, "중복된 학번입니다. 다시 확인해 주세요."),
    LOGIN_SUCCESS(HttpStatus.OK, true, "로그인에 성공했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, true, "로그아웃에 성공했습니다."),
    REISSUE_SUCCESS(HttpStatus.OK, true, "토큰을 재발급했습니다."),
    UPDATE_ROLE(HttpStatus.OK, true, "유저의 권한을 변경했습니다."),
    ADD_SEMESTER_SUCCESS(HttpStatus.CREATED, true, "학기를 추가했습니다."),
    DELETE_SEMESTER_SUCCESS(HttpStatus.OK, true, "학기를 삭제했습니다."),
    ADD_ACTIVITY_SUCCESS(HttpStatus.CREATED, true, "활동을 추가했습니다."),
    DELETE_ACTIVITY_SUCCESS(HttpStatus.OK, true, "활동을 삭제했습니다."),
    ADD_BOARD_SUCCESS(HttpStatus.CREATED, true, "게시판을 생성했습니다."),
    DELETE_BOARD_SUCCESS(HttpStatus.OK, true, "게시판을 삭제했습니다."),
    UPDATE_BOARD_SUCCESS(HttpStatus.OK, true, "게시판을 수정했습니다."),
    ADD_POST_SUCCESS(HttpStatus.CREATED, true, "게시글을 생성했습니다."),
    OK(HttpStatus.OK, true, "요청을 성공적으로 수행했습니다.");


    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
