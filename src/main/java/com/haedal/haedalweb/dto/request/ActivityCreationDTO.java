package com.haedal.haedalweb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityCreationDTO {
    @Schema(description = "활동명", example = "트랙")
    @Size(max = 15, message = "활동명은 최대 15글자까지 입력할 수 있습니다.")
    private String activityName;
}
