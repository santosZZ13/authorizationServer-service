package org.authorizationserver.exception;

public class NotFoundUserException extends ApiException {

	public NotFoundUserException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}

	public NotFoundUserException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}
}
