package yyl.demo.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import io.swagger.annotations.ApiOperation;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import yyl.demo.properties.SwaggerProperties;
import yyl.demo.properties.SwaggerProperties.ApiKeyInfo;

/**
 * 接口测试
 */
@Configuration
@ConditionalOnProperty(havingValue = "true", name = "custom.swagger.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableOpenApi
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    // ==============================Fields===========================================
    @Autowired
    private SwaggerProperties properties;

    // ==============================Methods==========================================
    /**
     * Swagger 插件
     * @param properties Swagger 配置项
     * @return Swagger 插件
     */
    @Bean
    @Primary
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)//
                .apiInfo(apiInfo())//
                .select()//
                .apis(selector())//
                .paths(paths())//
                .build()//
                .globalRequestParameters(globalRequestParameters());
    }

    private Predicate<RequestHandler> selector() {
        Predicate<RequestHandler> predicate = RequestHandlerSelectors.none();
        predicate = predicate.or(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        return predicate;
    }

    private Predicate<String> paths() {
        String pattern = properties.getPathPattern();
        if (StringUtils.isEmpty(pattern)) {
            return PathSelectors.any();
        } else {
            return PathSelectors.ant(pattern);
        }
    }

    private List<RequestParameter> globalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        for (ApiKeyInfo info : properties.getApiKeys()) {
            parameters.add(new RequestParameterBuilder()//
                    .name(info.getName())//
                    .in(ParameterType.from(info.getPassAs()))//
                    .description(info.getDescription())//
                    .required(false)//
                    .build());
        }
        return parameters;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()//
                .title(properties.getTitle())//
                .version(properties.getVersion())//
                .description(properties.getDescription())//
                .build();
    }
}
