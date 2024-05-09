package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공통 에러 코드 정의")
@RestController
public class CommonErrorController {

    @Operation(summary = "가상의 에러 코드 엔드포인트 (동작하지 않습니다.)")
    @ApiErrorCodeExamples({ErrorCode.NULL_REFRESH_TOKEN, ErrorCode.EXPIRED_REFRESH_TOKEN, ErrorCode.INVALID_REFRESH_TOKEN,
            ErrorCode.EXPIRED_ACCESS_TOKEN, ErrorCode.INVALID_ACCESS_TOKEN})
    @PostMapping("/common-error-code")
    public void commonErrorCodeDefinition() {
    }
}
