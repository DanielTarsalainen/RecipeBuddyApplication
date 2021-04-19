package com.example.recipebuddyapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecipeBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeBuddyApplication.class, args);
	}

	@Bean
	public CommandLineRunner recipeDemo() {
		return (args) -> {
		};
	}
}
