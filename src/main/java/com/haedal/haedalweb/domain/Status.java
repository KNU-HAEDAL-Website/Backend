package com.haedal.haedalweb.domain;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("활동"),
    INACTIVE("대기"),
    DELETED("삭제");

    private final String label;

    Status(String label) {
        this.label = label;
    }
}
