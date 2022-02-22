package de.bas.link.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DoubleValidator implements ConstraintValidator<DoubleConstraint, Double> {
    @Override
    public void initialize(DoubleConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
        return aDouble != null && aDouble >= 0.0;
    }
}
