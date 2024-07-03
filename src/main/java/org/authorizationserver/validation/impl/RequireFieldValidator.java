package org.authorizationserver.validation.impl;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.authorizationserver.validation.ValidField;

import java.util.List;
import java.util.Objects;

public class RequireFieldValidator implements ConstraintValidator<ValidField, Object> {
	@Override
	public void initialize(ValidField constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
		if (Objects.isNull(o)) {
			return false;
		}
		if (o instanceof String) {
			return !((String) o).isEmpty() && !((String) o).isBlank();
		} else if (o instanceof List) {
			return !((List<?>) o).isEmpty();
		}
		return false;
	}
//	@Override
//	public boolean isValid(String value, ConstraintValidatorContext context) {
//		return !Objects.isNull(value) &&
//				!value.isEmpty() &&
//				!value.isBlank();
//	}
}
