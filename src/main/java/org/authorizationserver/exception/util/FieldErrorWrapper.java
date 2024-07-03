package org.authorizationserver.exception.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldErrorWrapper {
	private String errorCode;
	private String field;
	private String message;
}
