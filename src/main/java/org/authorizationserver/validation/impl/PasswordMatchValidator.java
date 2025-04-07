package org.authorizationserver.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.validation.PasswordMatch;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegisterDto.Request> {
	@Override
	public boolean isValid(RegisterDto.Request request, ConstraintValidatorContext context) {
		String password = request.getPassword();
		String confirmPassword = request.getConfirmPassword();
		return password != null && password.equals(confirmPassword);
	}
}
