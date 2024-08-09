package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByBoardId(Long boardId);

    Optional<Post> findByBoardIdAndId(Long boardId, Long postId);
}
