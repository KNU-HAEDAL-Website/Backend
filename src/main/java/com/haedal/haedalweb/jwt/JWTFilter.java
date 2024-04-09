package com.haedal.haedalweb.jwt;

import com.haedal.haedalweb.constants.LoginConstants;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.dto.CustomUserDetails;
import com.haedal.haedalweb.dto.UserDetailsDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
// 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader(LoginConstants.ACCESS_TOKEN);

// 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print(LoginConstants.ACCESS_TOKEN_EXPIRED);

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

// 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals(LoginConstants.ACCESS_TOKEN)) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print(LoginConstants.INVALID_ACCESS_TOKEN);

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String userId = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                .id(userId)
                .role(Role.valueOf(role))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(userDetailsDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}