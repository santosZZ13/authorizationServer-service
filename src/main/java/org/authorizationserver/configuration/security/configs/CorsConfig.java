package org.authorizationserver.configuration.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration ccfg = new CorsConfiguration();
		ccfg.setAllowedOrigins(List.of("http://localhost:3000")); // Chỉ cho phép origin này
		ccfg.setAllowedMethods(Collections.singletonList("*")); // Cho phép tất cả phương thức (GET, POST, v.v.)
		ccfg.setAllowCredentials(true); // Cho phép gửi cookie/credentials
		ccfg.setAllowedHeaders(Collections.singletonList("*")); // Cho phép tất cả header
		ccfg.setExposedHeaders(Arrays.asList("Authorization")); // Phơi bày header Authorization nếu cần
		ccfg.setMaxAge(3600L); // Cache preflight request trong 1 giờ
		return request -> ccfg;
	}
}
