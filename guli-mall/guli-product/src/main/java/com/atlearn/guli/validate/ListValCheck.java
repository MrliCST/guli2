package com.atlearn.guli.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义校验注解：值必须在指定列表中
 *
 * @author mayao
 * @date 2026-08-03
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ListValCheckValidator.class })
public @interface ListValCheck {

    String message() default "{com.atlearn.guli.validate.ListValCheck.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 允许的值列表
     */
    int[] vals() default {};
}
