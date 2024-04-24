package com.haedal.haedalweb.service;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.LoginConstants;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.jwt.JWTUtil;
import com.haedal.haedalweb.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final JWTUtil jwtUtil;

    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, LoginConstants.REFRESH_TOKEN)
                .orElseThrow(() -> new BusinessException(ErrorCode.NULL_REFRESH_TOKEN));

        jwtUtil.validateRefreshToken(refreshToken);
        jwtUtil.deleteRefreshToken(refreshToken);
        CookieUtil.deleteByKey(response, LoginConstants.REFRESH_TOKEN);

        String userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        jwtUtil.issueToken(response, userId, role);
    }
}
