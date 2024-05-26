package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.request.LoginDTO;
import com.haedal.haedalweb.swagger.ApiErrorCodeExamples;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "로그인 관련 API")
@Controller
public class LoginController {

    @Operation(summary = "로그인 API")
    @ApiSuccessCodeExample(SuccessCode.LOGIN_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.INVALID_LOGIN_CONTENTS_TYPE, ErrorCode.FAILED_LOGIN})
    @PostMapping("/login")
    public void signIn(@RequestBody LoginDTO loginDTO) {
    }

    @Operation(summary = "로그아웃 API")
    @ApiSuccessCodeExample(SuccessCode.LOGOUT_SUCCESS)
    @PostMapping("/logout")
    public void signIn() {
    }
}
