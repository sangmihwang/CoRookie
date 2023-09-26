package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class PlanNotFoundException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public PlanNotFoundException() {
        this(ExceptionCode.PLAN_NOT_FOUND);
    }

    public PlanNotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
