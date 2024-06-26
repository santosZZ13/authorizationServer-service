package org.authorizationserver.validation.impl;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.authorizationserver.validation.ValidField;

import java.util.Objects;

public class RequireFieldValidator implements ConstraintValidator<ValidField, String> {
	@Override
	public void initialize(ValidField constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !Objects.isNull(value) &&
				!value.isEmpty() &&
				!value.isBlank();
	}
}
