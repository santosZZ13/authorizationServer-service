package org.authorizationserver.exception;

public class CannotConfirmCodeException extends ApiException {

	public CannotConfirmCodeException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}

	public CannotConfirmCodeException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}
}
