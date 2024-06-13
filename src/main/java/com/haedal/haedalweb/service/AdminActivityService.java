package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Activity;
import com.haedal.haedalweb.domain.Board;
import com.haedal.haedalweb.domain.Semester;
import com.haedal.haedalweb.dto.request.CreateActivityDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.ActivityRepository;
import com.haedal.haedalweb.repository.BoardRepository;
import com.haedal.haedalweb.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminActivityService {
    private final SemesterRepository semesterRepository;
    private final ActivityRepository activityRepository;
    private final BoardService boardService;

    @Transactional
    public void createActivity(Long semesterId, CreateActivityDTO createActivityDTO) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SEMESTER_ID));

        Activity activity = Activity.builder()
                .name(createActivityDTO.getActivityName())
                .semester(semester)
                .build();

        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ACTIVITY_ID));

        validateDeleteActivityRequest(activityId);
        activityRepository.delete(activity);
    }

    private void validateDeleteActivityRequest(Long activityId) {
        if (boardService.isActivityPresent(activityId)) {
            throw new BusinessException(ErrorCode.EXIST_BOARD);
        }
    }
}
