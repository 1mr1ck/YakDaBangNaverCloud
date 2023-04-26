package com.jxjtech.yakmanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {

        Info info = new Info()
                .title("YakDaBang RestAPI")
                .version(springdocVersion)
                .description("YakDaBang All RestAPI");

        // SecuritySecheme명
        String jwtSchemeName = "Authorization : AccessToken ( Bearer을 빼고 토큰만 등록 )";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("jwt token");
        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes("jwt token", new SecurityScheme()
                        .name("jwt token")
                        .type(SecurityScheme.Type.HTTP)// HTTP 방식
                        .description(jwtSchemeName)
                        .scheme("Bearer")
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    // 파라미터 전역설정
    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
//            Parameter param = new Parameter()
//                    .in(ParameterIn.HEADER.toString())  // 전역 헤더 설정
//                    .schema(new StringSchema()._default("pilling").name("AppID"))
//                    .name("AppID")
//                    .description("TEST AppID")
//                    .required(true);
//            operation.addParametersItem(param);
            return operation;
        };
    }

    @Bean
    public GroupedOpenApi all(OperationCustomizer operationCustomizer) { // 파라메타 추가
        return GroupedOpenApi.builder()
                .group("All RestAPI")
                .pathsToMatch("/**")
                .addOperationCustomizer(operationCustomizer)  // 전역 파라메타
                .build();
    }
}
