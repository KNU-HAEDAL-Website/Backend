package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.response.ActiveUserDTO;
import com.haedal.haedalweb.dto.response.InActiveUserDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminUserService {
    private final UserRepository userRepository;

    public List<ActiveUserDTO> getActiveUsers() {
        Sort sort = Sort.by(Sort.Order.asc("role"), Sort.Order.asc("name"));
        List<User> users = userRepository.findByUserStatus(UserStatus.ACTIVE, sort);

        return users.stream()
                .map(this::convertToActiveUserDTO)
                .collect(Collectors.toList());
    }

    public List<InActiveUserDTO> getInActiveUsers() {
        Sort sort = Sort.by(Sort.Order.asc("regDate"), Sort.Order.asc("name"));
        List<User> users = userRepository.findByUserStatus(UserStatus.INACTIVE, sort);

        return users.stream()
                .map(this::convertToInActiveUserDTO)
                .collect(Collectors.toList());
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

    @Transactional
    public void updateUserStatus(String userId, UserStatus userStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ID));

        if (user.getUserStatus() != UserStatus.ACTIVE && user.getUserStatus() != UserStatus.INACTIVE) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ID);
        }

        user.setUserStatus(userStatus);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ID));

        if (user.getUserStatus() != UserStatus.INACTIVE) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ID);
        }

        userRepository.delete(user);
    }

    @Transactional
    public void updateUserRole(String userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ID));

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ID);
        }

        user.setRole(role);
        userRepository.save(user);
    }
}
