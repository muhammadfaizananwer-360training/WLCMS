package com.softech.ls360.lcms.contentbuilder.model.validator.annotation;

import com.softech.ls360.lcms.contentbuilder.model.validator.NotEmptyValidator;
import com.softech.ls360.lcms.contentbuilder.model.validator.NotFalseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by abdul.wahid on 5/18/2016.
 */
@Documented
@Constraint(validatedBy = NotFalseValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFalse {


    String message() default "NotFalse";
    String[] messages();
    String[] properties();
    String[] verifiers();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
