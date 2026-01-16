package yyl.demo.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(//
        info = @Info(title = "API", version = "1.0", description = "接口文档"), //
        security = @SecurityRequirement(name = "AccessToken")//
)
@SecurityScheme(//
        name = "AccessToken", //
        type = SecuritySchemeType.APIKEY, //
        in = SecuritySchemeIn.HEADER, //
        paramName = "Access-Token"//
)
public class OpenApiConfiguration {
}
