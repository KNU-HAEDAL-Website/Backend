package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.Activity;
import com.haedal.haedalweb.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    boolean existsByActivityId(Long activityId);

    //@Query("select distinct b from Board b join fetch b.participants p join fetch p.user where b.activity.id = :activityId")
    Page<Board> findBoardsByActivity(Activity activity, Pageable pageable);

    Optional<Board> findByActivityIdAndId(Long activityId, Long boardId);
}
