package org.jaysabva.woc_crs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info().title("Course Registration System").description("Jay Sabva")
        );
    }
}
