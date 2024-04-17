package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.JoinDTO;
import com.haedal.haedalweb.dto.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.haedal.haedalweb.service.JoinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/join")
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> resisterUser(@RequestBody @Valid JoinDTO joinDTO) {
        joinService.createUserAccount(joinDTO);

        SuccessCode successCode = SuccessCode.JOIN_SUCCESS;
        SuccessResponse successResponse = SuccessResponse.builder()
                .success(successCode.getSuccess())
                .message(successCode.getMessage())
                .build();

        return ResponseEntity.status(successCode.getHttpStatus()).body(successResponse);
    }

    @GetMapping("/check-user-id")
    public ResponseEntity<SuccessResponse> checkUserIdDuplicate(@RequestParam String userId) {
        SuccessCode successCode = SuccessCode.UNIQUE_USER_ID;
        if (joinService.isUserIdDuplicate(userId)) {
            successCode = SuccessCode.DUPLICATED_USER_ID;
        }

        SuccessResponse successResponse = SuccessResponse.builder()
                .success(successCode.getSuccess())
                .message(successCode.getMessage())
                .build();

        return ResponseEntity.status(successCode.getHttpStatus()).body(successResponse);
    }

    @GetMapping("/check-student-number")
    public ResponseEntity<SuccessResponse> checkStudentNumberDuplicate(@RequestParam Integer studentNumber) {
        SuccessCode successCode = SuccessCode.UNIQUE_STUDENT_NUMBER;
        if (joinService.isStudentNumberDuplicate(studentNumber)) {
            successCode = SuccessCode.DUPLICATED_STUDENT_NUMBER;
        }

        SuccessResponse successResponse = SuccessResponse.builder()
                .success(successCode.getSuccess())
                .message(successCode.getMessage())
                .build();

        return ResponseEntity.status(successCode.getHttpStatus()).body(successResponse);
    }
}
