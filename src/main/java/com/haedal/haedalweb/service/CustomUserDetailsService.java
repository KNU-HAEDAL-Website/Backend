package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.domain.User;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.CustomUserDetails;
import com.haedal.haedalweb.dto.UserDetailsDTO;
import com.haedal.haedalweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.FAILED_LOGIN.getMessage()));

        if (user.getUserStatus() == UserStatus.DELETED || user.getUserStatus() == UserStatus.INACTIVE) {
            throw new UsernameNotFoundException(ErrorCode.FAILED_LOGIN.getMessage());
        }

        UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                .id(user.getId())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

        return new CustomUserDetails(userDetailsDTO);
    }
}
