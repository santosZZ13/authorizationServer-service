package org.authorizationserver.exception.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResponseError {
	private HttpStatus httpStatus;
	private String code;
	private String message;
	private String shortDesc;
}
