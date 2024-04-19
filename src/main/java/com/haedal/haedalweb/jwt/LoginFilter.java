package com.haedal.haedalweb.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.LoginConstants;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.ErrorResponse;
import com.haedal.haedalweb.dto.LoginDTO;
import com.haedal.haedalweb.dto.SuccessResponse;
import com.haedal.haedalweb.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO loginDTO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException(ErrorCode.INVALID_LOGIN_CONTENTS_TYPE.getMessage());
        }

        String userId = loginDTO.getUserId();
        String password = loginDTO.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);

        return authenticationManager.authenticate(authToken);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((int)LoginConstants.REFRESH_TOKEN_EXPIRATION_TIME_S);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        String userId = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwt(LoginConstants.ACCESS_TOKEN, userId, role, LoginConstants.ACCESS_TOKEN_EXPIRATION_TIME_MS);
        String refreshToken = jwtUtil.createJwt(LoginConstants.REFRESH_TOKEN, userId, role, LoginConstants.REFRESH_TOKEN_EXPIRATION_TIME_MS);

        redisService.saveRefreshToken(refreshToken, userId);

        response.setHeader(LoginConstants.ACCESS_TOKEN, accessToken);
        response.addCookie(createCookie(LoginConstants.REFRESH_TOKEN, refreshToken));
        response.setStatus(HttpStatus.OK.value());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        SuccessCode successCode = SuccessCode.LOGIN_SUCCESS;
        response.setStatus(successCode.getHttpStatus().value());
        SuccessResponse successResponse = SuccessResponse.builder()
                .success(true)
                .message(successCode.getMessage())
                .build();

        try {
            String jsonData = new ObjectMapper().writeValueAsString(successResponse);
            response.getWriter().write(jsonData);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (failed instanceof AuthenticationServiceException) {
            ErrorCode errorCode = ErrorCode.INVALID_LOGIN_CONTENTS_TYPE;
            response.setStatus(errorCode.getHttpStatus().value());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message(errorCode.getMessage())
                    .build();
            try {
                String jsonData = new ObjectMapper().writeValueAsString(errorResponse);
                response.getWriter().write(jsonData);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return;
        }

        ErrorCode errorCode = ErrorCode.FAILED_LOGIN;
        response.setStatus(errorCode.getHttpStatus().value());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errorCode.getMessage())
                .build();
        try {
            String jsonData = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().write(jsonData);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}