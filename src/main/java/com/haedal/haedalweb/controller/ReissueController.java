package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.service.IssueService;
import com.haedal.haedalweb.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReissueController {
    private final IssueService issueService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        issueService.reissueToken(request, response);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.REISSUE_SUCCESS);
    }
}
