package com.haedal.haedalweb.exception;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.dto.response.ErrorResponse;
import com.haedal.haedalweb.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

public class FilterExceptionHandler extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (BusinessException e) {
            sendErrorResponse(response, e);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        response.setStatus(errorCode.getHttpStatus().value());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ResponseUtil.writeAsJsonResponse(response, errorResponse);
    }
}