package com.haedal.haedalweb.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.dto.ErrorResponse;
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
            doFilter(request, response, chain);
        } catch (BusinessException e) {
            sendErrorResponse(request, response, e);
        }
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        response.setStatus(errorCode.getHttpStatus().value());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errorCode.getMessage())
                .build();
        try {
            String jsonData = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().write(jsonData);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
