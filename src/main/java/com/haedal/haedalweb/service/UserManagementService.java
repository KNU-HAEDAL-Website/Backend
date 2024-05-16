package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserManagementService {
    private final UserRepository userRepository;

    @Transactional
    public void updateUserStatusToActivated(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ID));

        if (user.getUserStatus() == UserStatus.INACTIVE) {
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ID));

        if (user.getUserStatus() == UserStatus.INACTIVE) {
            userRepository.delete(user);
        }
    }
}
