package com.member.koptani.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI memberServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Member Service API")
                        .description("Comprehensive REST API for managing members within the Koptani system. "
                                + "This documentation provides detailed information about available endpoints, "
                                + "request and response models, and error handling strategies.")
                        .version("v1.0.0")
                        .termsOfService("https://koptani.com/terms")
                        .contact(new Contact()
                                .name("Koptani Development Team")
                                .email("support@koptani.com")
                                .url("https://koptani.com/contact"))
                        .license(new License()
                                .name("Apache 2.0 License")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Full Documentation on GitHub")
                        .url("https://github.com/WizziGameDev/service-member-microservice"))
                .addTagsItem(new Tag()
                        .name("Members")
                        .description("Operations related to member management (CRUD)"));
    }
}
