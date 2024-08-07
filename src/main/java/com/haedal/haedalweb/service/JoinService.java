package com.haedal.haedalweb.service;

import com.haedal.haedalweb.domain.Profile;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.Sns;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.dto.request.JoinDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUserAccount(JoinDTO joinDTO) {
        String userId = joinDTO.getUserId();
        String password = joinDTO.getPassword();
        Integer studentNumber = joinDTO.getStudentNumber();
        String userName = joinDTO.getUserName();

        validateJoinRequest(joinDTO);

        User user = User.builder()
                .id(userId)
                .password(passwordEncoder.encode(password))
                .name(userName)
                .studentNumber(studentNumber)
                .role(Role.ROLE_MEMBER)
                .userStatus(UserStatus.INACTIVE)
                .regDate(LocalDateTime.now())
                .profile(createProfileWithSns())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void createAdminAccount(JoinDTO joinDTO) { // 관리자 회원가입 (개발용)
        String userId = joinDTO.getUserId();
        String password = joinDTO.getPassword();
        Integer studentNumber = joinDTO.getStudentNumber();
        String userName = joinDTO.getUserName();

        validateJoinRequest(joinDTO);

        User user = User.builder()
                .id(userId)
                .password(passwordEncoder.encode(password))
                .name(userName)
                .studentNumber(studentNumber)
                .role(Role.ROLE_ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .regDate(LocalDateTime.now())
                .profile(createProfileWithSns())
                .build();

        userRepository.save(user);
    }

    private void validateJoinRequest(JoinDTO joinDTO) {
        if (isUserIdDuplicate(joinDTO.getUserId())) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER_ID);
        }

        if (isStudentNumberDuplicate(joinDTO.getStudentNumber())) {
            throw new BusinessException(ErrorCode.DUPLICATED_STUDENT_NUMBER);
        }
    }

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsById(userId);
    }

    public boolean isStudentNumberDuplicate(Integer studentNumber) {
        return userRepository.existsByStudentNumber(studentNumber);
    }

    private Profile createProfileWithSns() {
        Sns sns = Sns.builder().build();

        return Profile.builder()
                .sns(sns)
                .build();
    }
}
