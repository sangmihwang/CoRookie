package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class ProjectNotFoundException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public ProjectNotFoundException() {
        this(ExceptionCode.PROJECT_NOT_FOUND);
    }

    public ProjectNotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
