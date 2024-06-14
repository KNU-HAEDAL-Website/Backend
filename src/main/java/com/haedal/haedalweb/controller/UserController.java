//package com.haedal.haedalweb.controller;
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
////
////@Tag(name = "User API")
////@RequestMapping("/users")
////@RequiredArgsConstructor
////@RestController
////public class UserController {
////
//////    @Operation(summary = "User 목록")
//////    @Parameters({
//////            @Parameter(name = "page", description = "현재 페이지"),
//////            @Parameter(name = "size", description = "한 페이지에 노출할 데이터 수")
//////    })
//////    @GetMapping
//////    public ResponseEntity<Page<ActiveUserDTO>> getUser(@RequestParam(value = "page", defaultValue = "0") int page,
//////                                                             @RequestParam(value = "size", defaultValue = "5") int size){
//////        Page<ActiveUserDTO> activeUsers;
//////        activeUsers = userService.getUsers(PageRequest.of(page, size, Sort.by(Order.asc("role"), Order.asc("name"))));
//////
//////        return ResponseEntity.ok(activeUsers);
//////    }
////}
