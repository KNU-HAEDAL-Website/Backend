package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.request.JoinDTO;
import com.haedal.haedalweb.dto.response.common.SuccessResponse;
import com.haedal.haedalweb.swagger.ApiErrorCodeExamples;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExample;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExamples;
import com.haedal.haedalweb.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.haedal.haedalweb.service.JoinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입 관련 API")
@RestController
@RequestMapping("/join")
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @Operation(summary = "회원가입")
    @ApiSuccessCodeExample(SuccessCode.JOIN_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.DUPLICATED_USER_ID, ErrorCode.DUPLICATED_STUDENT_NUMBER, ErrorCode.INVALID_PARAMETER})
    @PostMapping
    public ResponseEntity<SuccessResponse> resisterUser(@RequestBody @Valid JoinDTO joinDTO) {
        joinService.createUserAccount(joinDTO);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.JOIN_SUCCESS);
    }

    @Operation(summary = "관리자 회원가입 (개발용)")
    @ApiSuccessCodeExample(SuccessCode.JOIN_SUCCESS)
    @ApiErrorCodeExamples({ErrorCode.DUPLICATED_USER_ID, ErrorCode.DUPLICATED_STUDENT_NUMBER, ErrorCode.INVALID_PARAMETER})
    @PostMapping("/admin")
    public ResponseEntity<SuccessResponse> resisterAdmin(@RequestBody @Valid JoinDTO joinDTO) {
        joinService.createAdminAccount(joinDTO);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.JOIN_SUCCESS);
    }

    @Operation(summary = "ID 중복확인")
    @Parameter(name = "userId", description = "중복 확인할 ID")
    @ApiSuccessCodeExamples({SuccessCode.UNIQUE_USER_ID, SuccessCode.DUPLICATED_USER_ID})
    @GetMapping("/check-user-id")
    public ResponseEntity<SuccessResponse> checkUserIdDuplicate(@RequestParam String userId) {
        SuccessCode successCode = SuccessCode.UNIQUE_USER_ID;
        if (joinService.isUserIdDuplicate(userId)) {
            successCode = SuccessCode.DUPLICATED_USER_ID;
        }

        return ResponseUtil.buildSuccessResponseEntity(successCode);
    }

    @Operation(summary = "학번 중복확인")
    @Parameter(name = "studentNumber", description = "중복 확인할 학번")
    @ApiSuccessCodeExamples({SuccessCode.UNIQUE_STUDENT_NUMBER, SuccessCode.DUPLICATED_STUDENT_NUMBER})
    @GetMapping("/check-student-number")
    public ResponseEntity<SuccessResponse> checkStudentNumberDuplicate(@RequestParam Integer studentNumber) {
        SuccessCode successCode = SuccessCode.UNIQUE_STUDENT_NUMBER;
        if (joinService.isStudentNumberDuplicate(studentNumber)) {
            successCode = SuccessCode.DUPLICATED_STUDENT_NUMBER;
        }

        return ResponseUtil.buildSuccessResponseEntity(successCode);
    }
}
