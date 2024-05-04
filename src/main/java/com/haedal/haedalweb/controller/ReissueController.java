package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.service.IssueService;
import com.haedal.haedalweb.swagger.ApiErrorCodeExamples;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExample;
import com.haedal.haedalweb.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰 재발급")
@RequiredArgsConstructor
@RestController
public class ReissueController {
    private final IssueService issueService;

    @Operation(summary = "JWT 재발급")
    @ApiSuccessCodeExample(SuccessCode.REISSUE_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.NULL_REFRESH_TOKEN, ErrorCode.EXPIRED_REFRESH_TOKEN, ErrorCode.INVALID_REFRESH_TOKEN})
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        issueService.reissueToken(request, response);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.REISSUE_SUCCESS);
    }
}
