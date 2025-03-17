package org.authorizationserver.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.authorizationserver.common.BaseResponse;

public interface RegisterDto {
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class Request {
		private String email;
		private String password;
		private String confirmPassword;
		private String firstName;
		private String lastName;
		private String role;
	}


	@EqualsAndHashCode(callSuper = true)
	@SuperBuilder
	@Data
//	@AllArgsConstructor
//	@NoArgsConstructor
	class Response extends BaseResponse {
	}
}
