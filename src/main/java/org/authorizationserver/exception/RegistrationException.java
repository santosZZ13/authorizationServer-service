package org.authorizationserver.exception;

public class RegistrationException extends ApiException {

	public RegistrationException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}

	public RegistrationException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}
}
