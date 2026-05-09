package com.pizzai;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "PizzAI API",
                version = "1.0.0",
                description = "Sprint 3 - SOA e WebServices. API REST de pizzaria com recomendacoes inteligentes.",
                contact = @Contact(name = "PizzAI", email = "contato@pizzai.local")
        )
)
public class PizzaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaiApplication.class, args);
    }
}
