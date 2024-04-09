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
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(LoginConstants.REFRESH_TOKEN)) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return ResponseEntity.badRequest().body(LoginConstants.REFRESH_TOKEN_NULL);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return ResponseEntity.badRequest().body(LoginConstants.REFRESH_TOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(LoginConstants.REFRESH_TOKEN)) {

            //response status code
            return ResponseEntity.badRequest().body(LoginConstants.INVALID_REFRESH_TOKEN);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt(LoginConstants.ACCESS_TOKEN, username, role, 10*60*1000L);

        //response
        response.setHeader(LoginConstants.ACCESS_TOKEN, newAccess);

        return ResponseEntity.ok().build();
    }
}
