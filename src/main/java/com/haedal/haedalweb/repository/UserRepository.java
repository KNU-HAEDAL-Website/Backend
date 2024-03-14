package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByStudentNumber(Long id);
}
