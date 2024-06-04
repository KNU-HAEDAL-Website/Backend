package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.dto.response.SemesterDTO;
import com.haedal.haedalweb.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "학기 API")
@RequestMapping("/semesters")
@RequiredArgsConstructor
@RestController
public class SemesterController {
    private final SemesterService semesterService;

    @Operation(summary = "학기 전체 조회")
    @GetMapping
    public ResponseEntity<List<SemesterDTO>> getAllSemester() {
        List<SemesterDTO> semesterDTOs = semesterService.getAllSemester();

        return ResponseEntity.ok(semesterDTOs);
    }
}
