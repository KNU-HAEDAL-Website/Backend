package com.haedal.haedalweb.config;

import com.haedal.haedalweb.constants.ErrorCode;
import com.haedal.haedalweb.constants.ResponseCode;
import com.haedal.haedalweb.constants.SuccessCode;
import com.haedal.haedalweb.dto.response.common.ErrorResponse;
import com.haedal.haedalweb.dto.response.common.SuccessResponse;
import com.haedal.haedalweb.swagger.ApiErrorCodeExample;
import com.haedal.haedalweb.swagger.ApiErrorCodeExamples;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExample;
import com.haedal.haedalweb.swagger.ApiSuccessCodeExamples;
import com.haedal.haedalweb.swagger.ExampleHolder;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.FieldError;
import org.springframework.web.method.HandlerMethod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@OpenAPIDefinition(
        info = @Info(title = "HAEDAL-WEB API 명세서",
                description = "해달 웹 백엔드 API",
                version = "1.0")

)
@Configuration
public class SwaggerConfig {
    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        String accessToken = "Access Token (Bearer)";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(accessToken);

        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(BEARER_TOKEN_PREFIX)
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        Components components = new Components()
                .addSecuritySchemes(accessToken, accessTokenSecurityScheme);

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExamples apiErrorCodeExamples = handlerMethod.getMethodAnnotation(
                    ApiErrorCodeExamples.class);

            // @ApiErrorCodeExamples 어노테이션이 붙어있다면
            if (apiErrorCodeExamples != null) {
                generateResponseCodeResponseExample(operation, apiErrorCodeExamples.value());
            } else {
                ApiErrorCodeExample apiErrorCodeExample = handlerMethod.getMethodAnnotation(
                        ApiErrorCodeExample.class);

                // @ApiErrorCodeExamples 어노테이션이 붙어있지 않고
                // @ApiErrorCodeExample 어노테이션이 붙어있다면
                if (apiErrorCodeExample != null) {
                    generateResponseCodeResponseExample(operation, apiErrorCodeExample.value());
                }
            }

            ApiSuccessCodeExamples apiSuccessCodeExamples = handlerMethod.getMethodAnnotation(
                    ApiSuccessCodeExamples.class);

            if (apiSuccessCodeExamples != null) {
                generateResponseCodeResponseExample(operation, apiSuccessCodeExamples.value());
            } else {
                ApiSuccessCodeExample apiSuccessCodeExample = handlerMethod.getMethodAnnotation(
                        ApiSuccessCodeExample.class);

                if (apiSuccessCodeExample != null) {
                    generateResponseCodeResponseExample(operation, apiSuccessCodeExample.value());
                }
            }

            return operation;
        };
    }

    private void generateResponseCodeResponseExample(Operation operation, ResponseCode[] responseCodes) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(responseCodes)
                .map(
                        responseCode -> ExampleHolder.builder()
                                .holder(getSwaggerExample(responseCode))
                                .httpStatus(responseCode.getHttpStatus().value())
                                .name(responseCode.name())
                                .build()
                )
                .collect(Collectors.groupingBy(ExampleHolder::getHttpStatus));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    // 단일 에러 응답값 예시 추가
    private void generateResponseCodeResponseExample(Operation operation, ResponseCode responseCode) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder 객체 생성 및 ApiResponses에 추가
        ExampleHolder exampleHolder = ExampleHolder.builder()
                .holder(getSwaggerExample(responseCode))
                .name(responseCode.name())
                .httpStatus(responseCode.getHttpStatus().value())
                .build();

        addExamplesToResponses(responses, exampleHolder);
    }

    // ErrorResponseDto 형태의 예시 객체 생성
    private Example getSwaggerExample(ResponseCode responseCode) {
        Example example = new Example();

        if (responseCode instanceof ErrorCode) {
            List<ErrorResponse.ValidationError> validationErrorList = new ArrayList<>();

            if (responseCode == ErrorCode.INVALID_PARAMETER) {
                FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
                validationErrorList.add(ErrorResponse.ValidationError.of(fieldError));
            }

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .code(((ErrorCode) responseCode).getCode())
                    .message(responseCode.getMessage())
                    .errors(validationErrorList)
                    .build();

            example.setValue(errorResponse);

            return example;
        }


        SuccessResponse successResponse = SuccessResponse.builder()
                .success(((SuccessCode) responseCode).getSuccess())
                .message(responseCode.getMessage())
                .build();

        example.setValue(successResponse);

        return example;
    }

    private void addExamplesToResponses(ApiResponses responses,
                                        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {

//        if (responses != null && responses.containsKey("200")) {
//            responses.remove("200");
//        }

        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(),
                                    exampleHolder.getHolder()
                            )
                    );
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(status), apiResponse);
                }
        );
    }

    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.content(content);

//        if (responses != null && responses.containsKey("200")) {
//            responses.remove("200");
//        }

        responses.addApiResponse(String.valueOf(exampleHolder.getHttpStatus()), apiResponse);
    }
}