package org.authorizationserver.exception;

public class RedirectIsNotExistInAuthorizationCodeModeException extends ApiException {
	public RedirectIsNotExistInAuthorizationCodeModeException(String message, String code, String shortDesc) {
		super(message, code, shortDesc);
	}

	public RedirectIsNotExistInAuthorizationCodeModeException(String code, String shortDesc, String message, Throwable cause) {
		super(code, shortDesc, message, cause);
	}
}
