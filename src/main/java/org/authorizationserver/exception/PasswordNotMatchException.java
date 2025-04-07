package org.authorizationserver.exception;

public class PasswordNotMatchException extends ApiException {

	public PasswordNotMatchException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}

	public PasswordNotMatchException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}
}
