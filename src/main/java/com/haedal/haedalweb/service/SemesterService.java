package com.haedal.haedalweb.service;

import com.haedal.haedalweb.domain.Semester;
import com.haedal.haedalweb.dto.response.SemesterDTO;
import com.haedal.haedalweb.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SemesterService {
    private final SemesterRepository semesterRepository;

    public List<SemesterDTO> getAllSemester() {
        List<Semester> semesters = semesterRepository.findAll(Sort.by("name"));

        return semesters.stream()
                .map(this::convertToSemesterDTO)
                .collect(Collectors.toList());
    }

    private SemesterDTO convertToSemesterDTO(Semester semester) {
        return SemesterDTO.builder()
                .semesterId(semester.getId())
                .semesterName(semester.getName())
                .build();
    }
}
