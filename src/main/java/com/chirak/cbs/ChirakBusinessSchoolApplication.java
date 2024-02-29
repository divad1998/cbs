package com.chirak.cbs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

import java.util.List;

@SpringBootApplication
@OpenAPIDefinition
public class ChirakBusinessSchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChirakBusinessSchoolApplication.class, args);
	}
}
