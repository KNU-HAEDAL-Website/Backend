package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Semester;
import com.haedal.haedalweb.dto.request.SemesterCreationDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSemesterService {
    private final SemesterRepository semesterRepository;

    @Transactional
    public void createSemester(SemesterCreationDTO semesterCreationDTO) {
        validateAddSemesterRequest(semesterCreationDTO);

        String semesterName = semesterCreationDTO.getSemesterName();
        Semester semester = Semester.builder()
                .name(semesterName)
                .build();

        semesterRepository.save(semester);
    }

    private void validateAddSemesterRequest(SemesterCreationDTO semesterCreationDTO) {
        if (isSemesterDuplicate(semesterCreationDTO.getSemesterName())) {
            throw new BusinessException(ErrorCode.DUPLICATED_SEMESTER);
        }
    }

    public boolean isSemesterDuplicate(String semesterName) {
        return semesterRepository.existsByName(semesterName);
    }
}
