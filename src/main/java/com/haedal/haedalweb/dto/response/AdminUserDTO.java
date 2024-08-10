package com.haedal.haedalweb.dto.response;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserDTO {
    @Schema(description = "유저 아이디", example = "haedal12")
    private String userId;

    @Schema(description = "유저 학번", example = "2024111234")
    private Integer studentNumber;

    @Schema(description = "유저 이름", example = "조대성")
    private String userName;

    @Schema(description = "유저 권한", example = "(해구르르, 팀장, 일반)")
    private String role;

    @Schema(description = "가입 날짜")
    private LocalDateTime regDate;
}
