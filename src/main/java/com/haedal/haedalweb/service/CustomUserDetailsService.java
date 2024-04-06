package com.haedal.haedalweb.service;

import com.haedal.haedalweb.domain.User;
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
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id));

        UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                .id(user.getId())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

        return new CustomUserDetails(userDetailsDTO);
    }
}
