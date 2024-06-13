package com.haedal.haedalweb.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.net.URL;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreSignedUrlDTO {
    @Schema(description = "이미지 파일 저장 URL", example = "https://s3.ap-northeast-2.amazonaws.com/spring.cloud.aws.s3.bucket/~")
    private URL preSignedUrl;

    @Schema(description = "이미지 파일 Object Key", example = "boards/abc.jpg")
    private String imageUrl;
}
