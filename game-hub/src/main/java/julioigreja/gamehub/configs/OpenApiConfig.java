package julioigreja.gamehub.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Game Hub")
                .version("1.0")
                .description("An API for sharing games")
                .termsOfService("https://github.com/JulioEvencio/game-hub-backend/blob/main/UNLICENSE")
                .license(new License()
                        .name("UNLICENSE")
                        .url("https://github.com/JulioEvencio/game-hub-backend/blob/main/UNLICENSE")
                );

        return new OpenAPI().info(info);
    }

}
