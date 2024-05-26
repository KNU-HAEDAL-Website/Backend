package com.haedal.haedalweb.dto.request;

import com.haedal.haedalweb.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateDTO {
    @Schema(description = "유저 권한", example = "(해구르르, 팀장, 일반)")
    private String role;

    public Role getRole() {
        return Role.of(this.role);
    }
}
