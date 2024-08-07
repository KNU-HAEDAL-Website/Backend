package com.haedal.haedalweb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDTO {
    @Schema(description = "유저 아이디", example = "haedal12")
    private String userId;

    @Schema(description = "유저 비밀번호", example = "abc1234!")
    private String password;
}
