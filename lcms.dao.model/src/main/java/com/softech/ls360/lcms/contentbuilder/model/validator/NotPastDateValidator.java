package com.softech.ls360.lcms.contentbuilder.model.validator;

import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotEmpty;
import com.softech.ls360.lcms.contentbuilder.model.validator.annotation.NotPastDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 * Created by abdul.wahid on 5/18/2016.
 */
public class NotPastDateValidator implements ConstraintValidator<NotPastDate, Date> {

    @Override
    public void initialize(NotPastDate notPastDate) { }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext cxt) {
        return date == null || date.getTime() >= new Date().getTime();
    }

}
