package org.authorizationserver.exception;

public class ExpiredCodeException extends ApiException {

	public ExpiredCodeException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}

	public ExpiredCodeException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}
}
