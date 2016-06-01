package com.softech.ls360.lcms.contentbuilder.model.validator.annotation;

import com.softech.ls360.lcms.contentbuilder.model.validator.NotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import java.lang.annotation.*;

/**
 * Created by abdul.wahid on 5/18/2016.
 */
@Documented
@Constraint(validatedBy = NotEmptyValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {


    String message() default "empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
