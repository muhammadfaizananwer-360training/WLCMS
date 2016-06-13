package com.softech.ls360.lcms.contentbuilder.model.validator.annotation;

import com.softech.ls360.lcms.contentbuilder.model.validator.NotEmptyValidator;
import com.softech.ls360.lcms.contentbuilder.model.validator.NotPastDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by abdul.wahid on 5/18/2016.
 */
@Documented
@Constraint(validatedBy = NotPastDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotPastDate {


    String message() default "past date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
