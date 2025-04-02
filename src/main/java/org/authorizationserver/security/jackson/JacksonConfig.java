package org.authorizationserver.security.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
//		Fix jackson-datatype-jsr310 Java 8 date/time support
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}
}
