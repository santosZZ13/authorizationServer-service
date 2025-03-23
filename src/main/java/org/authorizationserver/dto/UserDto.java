package org.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public interface UserDto {
	class Request {

	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class Response {
		private ResponseData data;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class ResponseData {
		private String username;
		private Set<String> roles;
	}
}
