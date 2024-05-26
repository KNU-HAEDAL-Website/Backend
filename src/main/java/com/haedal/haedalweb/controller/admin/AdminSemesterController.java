package com.haedal.haedalweb.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 - 학기 관리 API")
@RequestMapping("/admin/semester")
@RequiredArgsConstructor
@RestController
public class AdminSemesterController {
}
