package com.dollop.app.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidater implements ConstraintValidator<ImageNameValid, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value.isBlank())
			return false;
		else 
			return true;
	}

}
