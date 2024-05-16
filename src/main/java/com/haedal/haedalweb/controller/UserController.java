package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.dto.ActiveUserDTO;
import com.haedal.haedalweb.dto.InActiveUserDTO;
import com.haedal.haedalweb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User 목록 API")
@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @Operation(summary = "활동중인 User 목록")
    @Parameters({
            @Parameter(name = "page", description = "현재 페이지"),
            @Parameter(name = "size", description = "한 페이지에 노출할 데이터 수")
    })
    @GetMapping("/active")
    public ResponseEntity<Page<ActiveUserDTO>> getActiveUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "5") int size){
        Page<ActiveUserDTO> activeUsers;
        activeUsers = userService.getActiveUsers(PageRequest.of(page, size, Sort.by(Order.asc("role"), Order.asc("name"))));

        return ResponseEntity.ok(activeUsers);
    }

    @Operation(summary = "가입 대기 중인 User 목록")
    @Parameters({
            @Parameter(name = "page", description = "현재 페이지"),
            @Parameter(name = "size", description = "한 페이지에 노출할 데이터 수")
    })
    @GetMapping("/inactive")
    public ResponseEntity<Page<InActiveUserDTO>> getInActiveUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "5") int size){
        Page<InActiveUserDTO> inActiveUsers;
        inActiveUsers = userService.getInActiveUsers(PageRequest.of(page, size, Sort.by(Order.asc("regDate"), Order.asc("name"))));

        return ResponseEntity.ok(inActiveUsers);
    }
}
