package com.haedal.haedalweb.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.response.common.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ResponseUtil() {}

    public static void sendSuccessResponse(HttpServletResponse response, SuccessCode successCode) {
        response.setStatus(successCode.getHttpStatus().value());

        SuccessResponse successResponse = SuccessResponse.builder()
                .success(successCode.getSuccess())
                .message(successCode.getMessage())
                .build();

        writeAsJsonResponse(response, successResponse);
    }

    public static ResponseEntity<SuccessResponse> buildSuccessResponseEntity(SuccessCode successCode) {
        SuccessResponse successResponse = SuccessResponse.builder()
                .success(successCode.getSuccess())
                .message(successCode.getMessage())
                .build();

        return ResponseEntity.status(successCode.getHttpStatus()).body(successResponse);
    }

    public static void writeAsJsonResponse(HttpServletResponse response, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonData);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}