package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Semester;
import com.haedal.haedalweb.dto.request.CreateSemesterDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSemesterService {
    private final SemesterRepository semesterRepository;
    private final ActivityService activityService;

    @Transactional
    public void createSemester(CreateSemesterDTO createSemesterDTO) {
        validateAddSemesterRequest(createSemesterDTO);

        String semesterName = createSemesterDTO.getSemesterName();
        Semester semester = Semester.builder()
                .name(semesterName)
                .build();

        semesterRepository.save(semester);
    }

    @Transactional
    public void deleteSemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SEMESTER_ID));

        validateDeleteSemesterRequest(semesterId);

        // 학기 안에 활동이 존재할 때, 에러 코드 반환하는 로직 작성
        semesterRepository.delete(semester);
    }

    private void validateAddSemesterRequest(CreateSemesterDTO createSemesterDTO) {
        if (isSemesterNameDuplicate(createSemesterDTO.getSemesterName())) {
            throw new BusinessException(ErrorCode.DUPLICATED_SEMESTER);
        }
    }

    private void validateDeleteSemesterRequest(Long semesterId) {
        if (activityService.isSemesterPresent(semesterId)) {
            throw new BusinessException(ErrorCode.EXIST_ACTIVITY);
        }
    }


    private boolean isSemesterNameDuplicate(String semesterName) {
        return semesterRepository.existsByName(semesterName);
    }
}
