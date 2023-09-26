package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class InvalidIssueProgressException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public InvalidIssueProgressException() {
        this(ExceptionCode.ISSUE_PRIORITY_INVALID);
    }

    public InvalidIssueProgressException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
