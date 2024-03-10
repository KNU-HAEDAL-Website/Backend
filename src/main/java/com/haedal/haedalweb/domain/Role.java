package com.haedal.haedalweb.domain;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("관리자"),
    STAFF("해구르르"),
    TEAM_LEADER("팀장"),
    MEMBER("일반"),
    CANDIDATE("예비");

    private final String label;

    Role(String label) {
        this.label = label;
    }
}