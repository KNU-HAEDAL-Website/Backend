package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findBySemesterId(Long semesterId);
    boolean existsBySemesterId(Long semesterId);
}
