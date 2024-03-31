package com.haedal.haedalweb.service;

import com.haedal.haedalweb.domain.Profile;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.Sns;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.dto.JoinDTO;
import com.haedal.haedalweb.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {
        String userId = joinDTO.getUserId();
        String password = joinDTO.getPassword();
        Integer studentNumber = joinDTO.getStudentNumber();
        String userName = joinDTO.getUserName();

        boolean isExistingStudentNumber = userRepository.existsByStudentNumber(studentNumber);
        boolean isExistingId = userRepository.existsById(userId);

        if (isExistingId || isExistingStudentNumber) {
            return;
        }

        User user = User.builder()
                .id(userId)
                .password(bCryptPasswordEncoder.encode(password))
                .name(userName)
                .studentNumber(studentNumber)
                .role(Role.ROLE_CANDIDATE)
                .profile(createProfileWithSns())
                .build();

        userRepository.save(user);
    }

    private Profile createProfileWithSns() {
        Sns sns = Sns.builder().build();

        return Profile.builder()
                .sns(sns)
                .build();
    }
}
