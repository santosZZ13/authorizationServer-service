package org.authorizationserver.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class GenericResponseErrorWrapper {
	private HttpStatus status;
	private String message;
	private List<FieldErrorWrapper> errors;

	public GenericResponseErrorWrapper(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public GenericResponseErrorWrapper(String message, List<FieldErrorWrapper> errors) {
		this.status = HttpStatus.BAD_REQUEST;
		this.message = message;
		this.errors = errors;
	}

	public GenericResponseErrorWrapper(HttpStatus status, String message, List<FieldErrorWrapper> errors) {
		super();
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public GenericResponseErrorWrapper(HttpStatus status, String message, FieldErrorWrapper error) {
		super();
		this.status = status;
		this.message = message;
		errors = Collections.singletonList(error);
	}
}
