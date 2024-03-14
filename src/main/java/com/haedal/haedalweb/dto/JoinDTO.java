package com.haedal.haedalweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {
    private String userId;
    private String password;
    private Long studentNumber;
    private String userName;
}