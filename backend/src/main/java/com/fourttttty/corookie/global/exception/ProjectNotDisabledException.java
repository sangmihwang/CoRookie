package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class ProjectNotDisabledException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public ProjectNotDisabledException() {
        this(ExceptionCode.PROJECT_NOT_DISABLED);
    }

    public ProjectNotDisabledException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
