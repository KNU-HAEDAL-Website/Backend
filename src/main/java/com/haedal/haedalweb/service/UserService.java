package com.haedal.haedalweb.service;

import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.ActiveUserDTO;
import com.haedal.haedalweb.dto.InActiveUserDTO;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<ActiveUserDTO> getActiveUsers(Pageable pageable) {
        Page<User> users = userRepository.findByUserStatus(UserStatus.ACTIVE, pageable);
        return users.map(this::convertToActiveUserDTO);
    }

    public Page<InActiveUserDTO> getInActiveUsers(Pageable pageable) {
        Page<User> users = userRepository.findByUserStatus(UserStatus.INACTIVE, pageable);
        return users.map(this::convertToInActiveUserDTO);
    }

    private ActiveUserDTO convertToActiveUserDTO(User user) {
        return ActiveUserDTO.builder()
                .userId(user.getId())
                .studentNumber(user.getStudentNumber())
                .userName(user.getName())
                .role(user.getRole().getLabel())
                .build();
    }

    private InActiveUserDTO convertToInActiveUserDTO(User user) {
        return InActiveUserDTO.builder()
                .userId(user.getId())
                .studentNumber(user.getStudentNumber())
                .userName(user.getName())
                .regDate(user.getRegDate())
                .build();
    }
}
