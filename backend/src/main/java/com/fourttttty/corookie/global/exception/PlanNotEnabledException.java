package com.fourttttty.corookie.global.exception;

import lombok.Getter;
@Getter
public class PlanNotEnabledException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public PlanNotEnabledException() {
        this(ExceptionCode.PLAN_NOT_ENABLED);
    }

    public PlanNotEnabledException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
