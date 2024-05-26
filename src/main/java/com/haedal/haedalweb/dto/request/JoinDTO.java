package com.haedal.haedalweb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinDTO {

    @Schema(description = "유저 아이디", example = "haedal12")
    @Size(min = 6, max = 12, message = "ID는 6자 이상 12자 이하여야 합니다.")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "ID는 영어와 숫자만 입력할 수 있습니다.")
    private String userId;

    @Schema(description = "유저 비밀번호", example = "abc1234!")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자(!@#$%^&*())를 혼용하여 8자 이상 20자 이하로 설정해야 합니다.")
    private String password;

    @Schema(description = "유저 학번", example = "2024111234")
    @Min(1_900_000_000)
    @Max(2_100_000_000)
    private Integer studentNumber;

    @Schema(description = "유저 이름", example = "조대성")
    @Size(min = 2, max = 5, message = "이름은 2글자 이상 5글자 이하여야 합니다.")
    private String userName;
}