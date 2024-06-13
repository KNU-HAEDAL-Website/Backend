package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.dto.CustomUserDetails;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_ID));
    }

    public List<User> findUserByIds(List<String> userIds) {
        return userRepository.findAllById(userIds);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        return findUserById(userId);
    }
}
