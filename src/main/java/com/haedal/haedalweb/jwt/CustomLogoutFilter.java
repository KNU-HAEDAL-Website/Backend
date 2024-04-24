package com.haedal.haedalweb.jwt;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.LoginConstants;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.exception.BusinessException;
import com.haedal.haedalweb.util.CookieUtil;
import com.haedal.haedalweb.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();

        if (!requestUri.matches("^/logout$") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = CookieUtil.getCookieValue(request, LoginConstants.REFRESH_TOKEN)
                .orElseThrow(() -> new BusinessException(ErrorCode.NULL_REFRESH_TOKEN));

        jwtUtil.validateRefreshToken(refreshToken);
        jwtUtil.deleteRefreshToken(refreshToken);
        CookieUtil.deleteByKey(response, LoginConstants.REFRESH_TOKEN);
        ResponseUtil.sendSuccessResponse(response, SuccessCode.LOGOUT_SUCCESS);
    }
}