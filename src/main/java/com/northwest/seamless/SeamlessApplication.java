package com.northwest.seamless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@OpenAPIDefinition(
        info = @Info(
                title = "Seemless HR NORTHWEST",
                version = "1.0",
                description = "API Documentation"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Provide JWT token. Example: `eyJhbGci...`"
)
@SpringBootApplication
public class SeamlessApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SeamlessApplication.class, args);
	}

        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
                return applicationBuilder.sources(SeamlessApplication.class);

        }

}
