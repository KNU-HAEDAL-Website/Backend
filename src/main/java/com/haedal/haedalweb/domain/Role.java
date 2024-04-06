package com.haedal.haedalweb.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_STAFF("해구르르"),
    ROLE_TEAM_LEADER("팀장"),
    ROLE_MEMBER("일반"),
    ROLE_CANDIDATE("예비");

    private final String label;

    Role(String label) {
        this.label = label;
    }
}