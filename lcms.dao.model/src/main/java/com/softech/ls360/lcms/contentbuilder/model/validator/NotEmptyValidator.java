package com.softech.ls360.lcms.contentbuilder.model.validator;

import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotEmpty;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by abdul.wahid on 5/18/2016.
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, Object> {

    @Override
    public void initialize(NotEmpty notEmpty) { }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext cxt) {
        return obj != null && !obj.toString().trim().equals("");
    }

}
