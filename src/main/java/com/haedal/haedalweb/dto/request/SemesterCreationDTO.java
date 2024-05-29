package com.haedal.haedalweb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SemesterCreationDTO {
    @Schema(description = "학기명", example = "20231")
    @Pattern(regexp = "^(20[0-9]{2}[12])$", message = "2000년 1학기부터 입력할 수 있습니다.")
    private String semesterName;
}
