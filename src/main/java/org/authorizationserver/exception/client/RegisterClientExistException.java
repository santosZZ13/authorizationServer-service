package org.authorizationserver.exception.client;

import org.authorizationserver.exception.util.ApiException;

public class RegisterClientExistException extends ApiException {
	public RegisterClientExistException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}

	public RegisterClientExistException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}
}
