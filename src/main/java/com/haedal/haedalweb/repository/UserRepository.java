package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByStudentNumber(Integer studentNumber);
    List<User> findByUserStatus(UserStatus userStatus, Sort sort);
}
