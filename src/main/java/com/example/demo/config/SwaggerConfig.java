package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI clinicOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinic Management API")
                        .description("REST API for managing patients, staff, beds and shifts in a care home facility")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kayu")
                                .email("ou1993072222@gmail.com")));
    }
}
