package com.haedal.haedalweb.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "관리자 - 체크 API")
@Controller
public class AdminValidatorController {
    @Operation(summary = "관리자 여부 체크")
    @GetMapping("/admin")
    public void checkAdmin() {
    }
}
