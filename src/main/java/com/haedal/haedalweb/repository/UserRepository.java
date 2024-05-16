package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByStudentNumber(Integer studentNumber);
    Page<User> findByUserStatus(UserStatus userStatus, Pageable pageable);
}
