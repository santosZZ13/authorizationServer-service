package org.authorizationserver.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.authorizationserver.validation.impl.RequireFieldValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RequireFieldValidator.class)
@Documented
public @interface ValidField {
	String message() default "";
	String code() default "";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
