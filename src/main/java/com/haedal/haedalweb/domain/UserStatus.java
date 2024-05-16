package com.haedal.haedalweb.domain;

import lombok.Getter;

@Getter
public enum UserStatus {
    MASTER("웹관리자"),
    ACTIVE("활동"),
    INACTIVE("대기"),
    DELETED("삭제");

    private final String label;

    UserStatus(String label) {
        this.label = label;
    }
}
