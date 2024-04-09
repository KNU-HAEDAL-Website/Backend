package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.constants.LoginConstants;
import com.haedal.haedalweb.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReissueController {

    private final JWTUtil jwtUtil;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(LoginConstants.REFRESH_TOKEN)) {

                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {

            //response status code
            return ResponseEntity.badRequest().body(LoginConstants.REFRESH_TOKEN_NULL);
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            return ResponseEntity.badRequest().body(LoginConstants.REFRESH_TOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals(LoginConstants.REFRESH_TOKEN)) {

            //response status code
            return ResponseEntity.badRequest().body(LoginConstants.INVALID_REFRESH_TOKEN);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        //make new JWT
        String newAccessToken = jwtUtil.createJwt(LoginConstants.ACCESS_TOKEN, username, role, LoginConstants.ACCESS_TOKEN_EXPIRATION_TIME_MS);
        String newRefreshToken = jwtUtil.createJwt(LoginConstants.REFRESH_TOKEN, username, role, LoginConstants.REFRESH_TOKEN_EXPIRATION_TIME_MS);

        //response
        response.setHeader(LoginConstants.ACCESS_TOKEN, newAccessToken);
        response.addCookie(createCookie(LoginConstants.REFRESH_TOKEN, newRefreshToken));

        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(LoginConstants.REFRESH_TOKEN_COOKIE_EXPIRATION_TIME);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
