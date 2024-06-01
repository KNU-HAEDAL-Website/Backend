package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    boolean existsByName(String name);
}
