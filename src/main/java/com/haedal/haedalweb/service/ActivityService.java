package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Activity;
import com.haedal.haedalweb.domain.Semester;
import com.haedal.haedalweb.dto.response.ActivityDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public List<ActivityDTO> getActivities(Long semesterId) {
        List<Activity> activities = activityRepository.findBySemesterId(semesterId);

        return activities.stream()
                .map((activity) -> convertToActivityDTO(activity, semesterId))
                .collect(Collectors.toList());
    }

    private ActivityDTO convertToActivityDTO(Activity activity, Long semesterId) {
        return ActivityDTO.builder()
                .activityId(activity.getId())
                .activityName(activity.getName())
                .semesterId(semesterId)
                .build();
    }

}
