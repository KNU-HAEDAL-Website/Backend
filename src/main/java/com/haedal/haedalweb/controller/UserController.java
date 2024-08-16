package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.dto.response.user.UserDTO;
import com.haedal.haedalweb.dto.response.user.UserSummaryDTO;
import com.haedal.haedalweb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "유저 API")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @Operation(summary = "User Me 정보 조회")
    @GetMapping("/users/me")
    public ResponseEntity<UserDTO> getMe() {
        UserDTO user = userService.getMe();

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "User 목록 (학번 포함)")
    @GetMapping("/private/users")
    public ResponseEntity<List<UserSummaryDTO>> getUser(){
        List<UserSummaryDTO> users = userService.getUsers();

        return ResponseEntity.ok(users);
    }

    //    @Operation(summary = "User 목록")
//    @Parameters({
//            @Parameter(name = "page", description = "현재 페이지"),
//            @Parameter(name = "size", description = "한 페이지에 노출할 데이터 수")
//    })
//    @GetMapping
//    public ResponseEntity<Page<UserDTO>> getUser(@RequestParam(value = "page", defaultValue = "0") int page,
//                                                      @RequestParam(value = "size", defaultValue = "5") int size){
//        Page<UserDTO> activeUsers;
//        activeUsers = userService.getUsers(PageRequest.of(page, size, Sort.by(Order.asc("role"), Order.asc("name"))));
//
//        return ResponseEntity.ok(activeUsers);
//    }
}
