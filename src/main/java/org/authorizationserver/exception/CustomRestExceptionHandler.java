package org.authorizationserver.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
																  @NotNull HttpHeaders headers,
																  @NotNull HttpStatusCode status, @NotNull WebRequest request) {
		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		List<FieldErrorWrapper> fieldErrorWrappers = new ArrayList<>();

		fieldErrors.forEach(fieldError -> {
			FieldErrorWrapper fieldErrorWrapper = new FieldErrorWrapper();
			String errorCode = getErrorCode(fieldError.getArguments());
			fieldErrorWrapper.setErrorCode(errorCode);
			fieldErrorWrapper.setField(fieldError.getField());
			fieldErrorWrapper.setMessage(fieldError.getDefaultMessage());
			fieldErrorWrappers.add(fieldErrorWrapper);
		});

		return ResponseEntity.badRequest().body(GenericResponseErrorWrapper
				.builder()
				.errors(fieldErrorWrappers)
				.message("Validation failed")
				.status(HttpStatus.BAD_REQUEST)
				.build());
	}

	private String getErrorCode(Object[] arguments) {
		if (Objects.nonNull(arguments) && arguments.length > 0) {
			return arguments[1].toString();
		}
		return null;
	}
}
