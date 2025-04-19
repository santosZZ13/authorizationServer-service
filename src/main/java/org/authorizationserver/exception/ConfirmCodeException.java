package org.authorizationserver.exception;

public class ConfirmCodeException extends ApiException {

	public ConfirmCodeException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}

	public ConfirmCodeException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}
}
