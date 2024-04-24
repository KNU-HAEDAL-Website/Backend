package com.haedal.haedalweb.jwt;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.LoginConstants;
import com.haedal.haedalweb.domain.Role;
import com.haedal.haedalweb.dto.CustomUserDetails;
import com.haedal.haedalweb.dto.UserDetailsDTO;
import com.haedal.haedalweb.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(LoginConstants.ACCESS_TOKEN);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split(" ")[1];;
        validateAccessToken(accessToken);

        String userId = jwtUtil.getUserId(accessToken);
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

    private void validateAccessToken(String accessToken) {
        String category;

        try {
            category = jwtUtil.getCategory(accessToken);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        if (!category.equals(LoginConstants.ACCESS_TOKEN)) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}