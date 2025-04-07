package org.authorizationserver.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseError {
	private String code;
	private String message;
	private String shortDesc;
}
