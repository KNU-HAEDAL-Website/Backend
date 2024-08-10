package com.haedal.haedalweb.service.admin;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.response.user.AdminUserDTO;
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

    public List<AdminUserDTO> getUsers(UserStatus userStatus, Sort sort) {
        List<User> users = userRepository.findByUserStatus(userStatus, sort);

        return users.stream()
                .map(this::convertToAdminUserDTO)
                .collect(Collectors.toList());
    }

    private AdminUserDTO convertToAdminUserDTO(User user) {
        return AdminUserDTO.builder()
                .userId(user.getId())
                .studentNumber(user.getStudentNumber())
                .userName(user.getName())
                .role(user.getRole().getLabel())
                .regDate(user.getRegDate())
                .build();
    }

    @Transactional
    public void updateUserStatus(String userId, UserStatus userStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_ID));

        if (user.getUserStatus() != UserStatus.ACTIVE && user.getUserStatus() != UserStatus.INACTIVE) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER_ID);
        }

        user.setUserStatus(userStatus);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_ID));

        if (user.getUserStatus() != UserStatus.INACTIVE) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER_ID);
        }

        userRepository.delete(user);
    }

    @Transactional
    public void updateUserRole(String userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_ID));

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER_ID);
        }

        user.setRole(role);
        userRepository.save(user);
    }
}
