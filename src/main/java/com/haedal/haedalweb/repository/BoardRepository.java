package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    boolean existsByActivityId(Long activityId);
}
