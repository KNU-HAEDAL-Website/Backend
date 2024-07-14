package com.haedal.haedalweb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateBoardDTO {
    @Schema(description = "게시판 이름", example = "게시판1")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "영어, 숫자, 한글만 입력가능합니다.")
    @Size(min = 1, max = 15, message = "게시판 이름은 1자 이상 15자 이하여야 합니다.")
    private String boardName;

    @Schema(description = "게시판 소개", example = "이 게시판에 대한 소개글")
    @Size(max = 50, message = "게시판 소개는 50자 이하여야 합니다.")
    private String boardIntro;

    @Schema(description = "참여 인원 ID", allowableValues = {"haedal1234", "good1234"})
    private Set<String> participants;
}
