package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class IssueNotFoundException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public IssueNotFoundException() {
        this(ExceptionCode.ISSUE_NOT_FOUND);
    }

    public IssueNotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
