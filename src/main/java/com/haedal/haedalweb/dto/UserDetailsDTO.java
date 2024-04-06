package com.haedal.haedalweb.dto;

import com.haedal.haedalweb.domain.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserDetailsDTO {
    private String id;
    private String password;
    private Role role;
}
