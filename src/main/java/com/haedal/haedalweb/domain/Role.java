package com.haedal.haedalweb.domain;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.exception.BusinessException;
import lombok.Getter;
import java.util.Arrays;

@Getter
public enum Role {
    ROLE_WEB_MASTER("관리자"),
    ROLE_ADMIN("해구르르"),
    ROLE_TEAM_LEADER("팀장"),
    ROLE_MEMBER("일반");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public static Role of(String label) {
        return Arrays.stream(Role.values())
                .filter(role -> role.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ROLE));
    }
}