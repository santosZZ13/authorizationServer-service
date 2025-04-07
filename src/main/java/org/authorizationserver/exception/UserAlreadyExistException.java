package org.authorizationserver.exception;

public class UserAlreadyExistException extends ApiException {
	public UserAlreadyExistException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}

	public UserAlreadyExistException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}
}
