package yyl.demo.configuration;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import yyl.demo.properties.SwaggerProperties;
import yyl.demo.properties.SwaggerProperties.GlobalHeaderInfo;

/**
 * 接口测试
 */
@ConditionalOnProperty(havingValue = "true", name = "custom.swagger.enabled", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {

    // ==============================Fields===========================================
    @Autowired
    private SwaggerProperties properties;

    // ==============================Methods==========================================
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()//
                .title(properties.getTitle())//
                .version(properties.getVersion())//
                .description(properties.getDescription())//
        );
    }

    @Bean
    OperationCustomizer customOperationCustomizer() {
        return new OperationCustomizer() {
            @Override
            public Operation customize(Operation operation, HandlerMethod handlerMethod) {
                for (GlobalHeaderInfo info : properties.getGlobalHeaders()) {
                    operation.addParametersItem(new Parameter()//
                            .in(ParameterIn.HEADER.toString())//
                            .name(info.getName())//
                            .description(info.getDescription())//
                            .schema(new StringSchema())//
                    );
                }
                return operation;
            }
        };
    }
}
