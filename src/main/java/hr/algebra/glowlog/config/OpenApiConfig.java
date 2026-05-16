package hr.algebra.glowlog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI glowlogOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("GlowLog REST API")
                .version("1.0.0")
                .description("Personal skincare product tracker and routine journal. Use /api/auth/login to obtain JWT tokens.")
                .contact(new Contact()
                    .name("GlowLog Dev Team")
                    .email("dev@glowlog.hr")))
            .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
            .components(new Components()
                .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Paste your access token here")));
    }
}
