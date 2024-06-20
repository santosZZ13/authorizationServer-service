package org.authorizationserver.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.authorizationserver.validation.ValidAuthorizationCodeTimeToLive;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

public class ValidAuthorizationCodeTimeToLiveValidator implements ConstraintValidator<ValidAuthorizationCodeTimeToLive, String> {
	@Override
	public void initialize(ValidAuthorizationCodeTimeToLive constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	/**
	 * Set the time-to-live for an authorization code. Must be greater than {@code Duration.ZERO}.
	 * A maximum authorization code lifetime of 10 minutes is RECOMMENDED.
	 * The {@link TokenSettings.Builder} for further configuration
	 * @since 0.4.0
	 */
	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return false;
	}
}
