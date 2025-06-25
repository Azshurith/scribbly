package com.devitrax.scribbly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main entry point for the Scribbly Spring Boot application.
 * <p>
 * This class boots the application and enables Spring's caching mechanism.
 */
@SpringBootApplication
@EnableCaching
public class ScribblyApplication {

	/**
	 * Starts the Scribbly application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ScribblyApplication.class, args);
	}
}
