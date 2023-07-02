package com.audition;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Audition API",
                description = "Test general knowledge of SpringBoot, Java, Gradle etc.",
                version = "v1.0",
                contact = @Contact(
                        name = "Vito Lefemine",
                        email = "vito.lefemine@gmail.com"
                )
        )
)
public class AuditionApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AuditionApplication.class, args);
    }

}
