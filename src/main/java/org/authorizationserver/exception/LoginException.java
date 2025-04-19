package org.authorizationserver.exception;

public class LoginException extends ApiException {

	public LoginException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}

	public LoginException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}
}