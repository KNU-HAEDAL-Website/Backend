package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.CustomUserDetails;
import com.haedal.haedalweb.dto.response.user.UserDTO;
import com.haedal.haedalweb.dto.response.user.UserSummaryDTO;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserSummaryDTO> getUsers() {
        List<User> users = userRepository.findByUserStatus(UserStatus.ACTIVE, null);

        return users.stream()
                .map(this::convertToPrivateUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User user = getLoggedInUser();

        return convertToUserDTO(user);
    }

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

    public UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getId())
                .studentNumber(user.getStudentNumber())
                .userName(user.getName())
                .role(user.getRole().getLabel())
                .regDate(user.getRegDate())
                .build();
    }

    private UserSummaryDTO convertToPrivateUserDTO(User user) {
        return UserSummaryDTO.builder()
                .userId(user.getId())
                .studentNumber(user.getStudentNumber())
                .userName(user.getName())
                .build();
    }
}
