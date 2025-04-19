package org.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public interface UserInfoDto {
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
		private String email;
		private String firstName;
		private String lastName;
		private String locale;
		private String avatarUrl;
		private String emailVerified;
		private String active;
		private String provide;
		private Set<String> roles;
	}
}
