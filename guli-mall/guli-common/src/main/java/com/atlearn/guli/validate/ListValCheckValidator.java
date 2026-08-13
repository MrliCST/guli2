package com.atlearn.guli.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ListValCheck 校验器：判断值是否在 vals 允许列表中
 *
 * @author mayao
 * @date 2026-08-03
 */
public class ListValCheckValidator implements ConstraintValidator<ListValCheck, Long> {

    private Set<Long> allowedVals;

    @Override
    public void initialize(ListValCheck annotation) {
        allowedVals = Arrays.stream(annotation.vals())
            .mapToLong(v -> v)
            .boxed()
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        // 允许 null，交 @NotNull 单独校验
        if (value == null) {
            return true;
        }
        return allowedVals.contains(value);
    }
}
