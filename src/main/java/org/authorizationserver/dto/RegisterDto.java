package org.authorizationserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.authorizationserver.common.BaseResponse;
import org.authorizationserver.validation.PasswordMatch;

public interface RegisterDto {
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@PasswordMatch
	class Request {
		@NotBlank(message = "Email is required")
		@Email(message = "Email must be a valid email address")
		private String email;

		@NotBlank(message = "Password is required")
		@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
		@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$",
				message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace")
		private String password;

		@NotBlank(message = "Confirm password is required")
		private String confirmPassword;

		@NotBlank(message = "First name is required")
		@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
		private String firstName;

		@NotBlank(message = "Last name is required")
		@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
		private String lastName;

		//		@NotBlank(message = "Birth month is required")
//		@Pattern(regexp = "^([1-9]|1[0-2])$", message = "Birth month must be between 1 and 12")
		private Integer birthMonth;

		//		@NotBlank(message = "Birth day is required")
//		@Pattern(regexp = "^([1-9]|[12][0-9]|3[01])$", message = "Birth day must be between 1 and 31")
		private Integer birthDay;

		//		@NotBlank(message = "Birth year is required")
//		@Pattern(regexp = "^(19[0-9]{2}|20[0-2][0-5])$", message = "Birth year must be between 1900 and 2025")
		private Integer birthYear;
	}


	@EqualsAndHashCode(callSuper = true)
	@SuperBuilder
	@Data
//	@AllArgsConstructor
//	@NoArgsConstructor
	class Response extends BaseResponse {
	}
}
