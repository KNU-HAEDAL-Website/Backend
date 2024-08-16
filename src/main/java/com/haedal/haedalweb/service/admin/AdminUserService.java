package com.haedal.haedalweb.service.admin;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.response.user.UserDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.UserRepository;
import com.haedal.haedalweb.service.UserService;
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
    private final UserService userService;

    public List<UserDTO> getUsers(UserStatus userStatus, Sort sort) {
        List<User> users = userRepository.findByUserStatus(userStatus, sort);

        return users.stream()
                .map(userService::convertToUserDTO)
                .collect(Collectors.toList());
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
