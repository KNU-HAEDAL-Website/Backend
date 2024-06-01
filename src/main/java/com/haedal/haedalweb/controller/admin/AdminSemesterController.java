package com.haedal.haedalweb.controller.admin;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.request.SemesterCreationDTO;
import com.haedal.haedalweb.dto.response.SuccessResponse;
import com.haedal.haedalweb.service.AdminSemesterService;
import com.haedal.haedalweb.swagger.ApiErrorCodeExample;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExample;
import com.haedal.haedalweb.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 - 학기 관리 API")
@RequestMapping("/admin/semesters")
@RequiredArgsConstructor
@RestController
public class AdminSemesterController {
    private final AdminSemesterService adminSemesterService;

    @Operation(summary = "학기 추가")
    @ApiSuccessCodeExample(SuccessCode.ADD_SEMESTER_SUCCESS)
    @ApiErrorCodeExample(ErrorCode.DUPLICATED_SEMESTER)
    @PostMapping
    public ResponseEntity<SuccessResponse> addSemester(@RequestBody @Valid SemesterCreationDTO semesterCreationDTO) {
        adminSemesterService.createSemester(semesterCreationDTO);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.ADD_SEMESTER_SUCCESS);
    }

    @Operation(summary = "학기 삭제")
    @ApiSuccessCodeExample(SuccessCode.DELETE_SEMESTER_SUCCESS)
    @ApiErrorCodeExample(ErrorCode.NOT_FOUND_SEMESTER_ID)
    @Parameter(name = "semesterId", description = "삭제할 학기 ID")
    @DeleteMapping("/{semesterId}")
    public ResponseEntity<SuccessResponse> deleteSemester(@PathVariable Long semesterId) {
        adminSemesterService.deleteSemester(semesterId);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.DELETE_SEMESTER_SUCCESS);
    }

}
