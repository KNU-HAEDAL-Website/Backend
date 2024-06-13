package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.dto.response.ActivityDTO;
import com.haedal.haedalweb.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "활동 API")
@RequiredArgsConstructor
@RestController
public class ActivityController {
    private final ActivityService activityService;

    @Operation(summary = "해당 학기 활동 조회")
    @GetMapping("/semesters/{semesterId}/activities")
    public ResponseEntity<List<ActivityDTO>> getActivities(@PathVariable Long semesterId) {
        List<ActivityDTO> activityDTOs = activityService.getActivityDTOs(semesterId);

        return ResponseEntity.ok(activityDTOs);
    }
}
