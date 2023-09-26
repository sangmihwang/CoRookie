package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class ProjectNotEnabledException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public ProjectNotEnabledException() {
        this(ExceptionCode.PLAN_NOT_FOUND);
    }

    public ProjectNotEnabledException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
