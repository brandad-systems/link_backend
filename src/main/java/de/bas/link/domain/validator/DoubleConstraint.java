package de.bas.link.domain.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DoubleValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleConstraint {
    String message() default "Invalid value. Must be greater than 0.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}