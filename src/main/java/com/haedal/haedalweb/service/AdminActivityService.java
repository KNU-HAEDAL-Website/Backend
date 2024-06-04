package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Activity;
import com.haedal.haedalweb.domain.Semester;
import com.haedal.haedalweb.dto.request.ActivityCreationDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.ActivityRepository;
import com.haedal.haedalweb.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminActivityService {
    private final SemesterRepository semesterRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public void createActivity(Long semesterId, ActivityCreationDTO activityCreationDTO) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SEMESTER_ID));

        Activity activity = Activity.builder()
                .name(activityCreationDTO.getActivityName())
                .semester(semester)
                .build();

        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long semesterId, Long activityId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SEMESTER_ID));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ACTIVITY_ID));

        // 활동안에 게시판이 존재할 때, 에러 코드 반환하는 로직 작성
        activityRepository.delete(activity);
    }
}
