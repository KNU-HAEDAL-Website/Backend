package com.haedal.haedalweb.controller.admin;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.domain.UserStatus;
import com.haedal.haedalweb.dto.response.user.UserDTO;
import com.haedal.haedalweb.dto.request.UpdateRoleDTO;
import com.haedal.haedalweb.dto.response.common.SuccessResponse;
import com.haedal.haedalweb.service.admin.AdminUserService;
import com.haedal.haedalweb.swagger.ApiErrorCodeExample;
import com.haedal.haedalweb.swagger.ApiErrorCodeExamples;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExample;
import com.haedal.haedalweb.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@Tag(name = "관리자 - 유저 관리 API")
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@RestController
public class AdminUserController {
    private final AdminUserService adminUserService;

    @Operation(summary = "User 목록")
    @Parameter(name = "active", description = "활동 유저 true, 가입대기 유저 false")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUser(@RequestParam Boolean active){
        List<UserDTO> users = null;

        if (active) {
            Sort sort = Sort.by(Sort.Order.asc("role"), Sort.Order.asc("name"));
            users = adminUserService.getUsers(UserStatus.ACTIVE, sort);
        } else {
            Sort sort = Sort.by(Sort.Order.asc("regDate"), Sort.Order.asc("name"));
            users = adminUserService.getUsers(UserStatus.INACTIVE, sort);
        }

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "가입 승인")
    @ApiSuccessCodeExample(SuccessCode.JOIN_APPROVAL)
    @ApiErrorCodeExample(ErrorCode.NOT_FOUND_USER_ID)
    @Parameter(name = "userId", description = "가입 승인할 유저 ID")
    @PatchMapping("/{userId}/approve")
    public ResponseEntity<SuccessResponse> approveUser(@PathVariable String userId) {
        adminUserService.updateUserStatus(userId, UserStatus.ACTIVE);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.JOIN_APPROVAL);
    }

    @Operation(summary = "가입 거절")
    @ApiSuccessCodeExample(SuccessCode.JOIN_REFUSAL)
    @ApiErrorCodeExample(ErrorCode.NOT_FOUND_USER_ID)
    @Parameter(name = "userId", description = "가입 거절할 유저 ID")
    @DeleteMapping("/{userId}/reject")
    public ResponseEntity<SuccessResponse> rejectUser(@PathVariable String userId) {
        adminUserService.deleteUser(userId);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.JOIN_REFUSAL);
    }

    @Operation(summary = "유저 내보내기")
    @ApiSuccessCodeExample(SuccessCode.EXPEL_USER)
    @ApiErrorCodeExample(ErrorCode.NOT_FOUND_USER_ID)
    @Parameter(name = "userId", description = "내보낼 유저 ID")
    @PatchMapping("/{userId}/expel")
    public ResponseEntity<SuccessResponse> expelUser(@PathVariable String userId) {
        adminUserService.updateUserStatus(userId, UserStatus.DELETED);

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.EXPEL_USER);
    }

    @Operation(summary = "유저 권한 변경")
    @ApiSuccessCodeExample(SuccessCode.UPDATE_ROLE)
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_USER_ID, ErrorCode.NOT_FOUND_ROLE})
    @Parameter(name = "userId", description = "권한 변경할 유저 ID")
    @PatchMapping("/{userId}/role")
    public ResponseEntity<SuccessResponse> changeUserRole(@PathVariable String userId, @RequestBody UpdateRoleDTO updateRoleDTO) {
        adminUserService.updateUserRole(userId, updateRoleDTO.getRole());

        return ResponseUtil.buildSuccessResponseEntity(SuccessCode.UPDATE_ROLE);
    }
}
