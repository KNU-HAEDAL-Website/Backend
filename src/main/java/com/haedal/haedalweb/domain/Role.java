package com.haedal.haedalweb.domain;

import lombok.Getter;

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
}