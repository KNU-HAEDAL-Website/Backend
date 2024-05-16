package com.haedal.haedalweb.service;

import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.ActiveUserDTO;
import com.haedal.haedalweb.dto.InActiveUserDTO;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


//    public Page<ActiveUserDTO> getUsers(Pageable pageable) {
//        Page<User> users = userRepository.findByUserStatus(UserStatus.ACTIVE, pageable);
//        return users.map(this::convertToActiveUserDTO);
//    }

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
}
