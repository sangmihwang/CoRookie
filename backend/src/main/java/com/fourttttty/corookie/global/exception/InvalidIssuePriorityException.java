package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class InvalidIssuePriorityException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public InvalidIssuePriorityException() {
        this(ExceptionCode.ISSUE_PROGRESS_INVALID);
    }

    public InvalidIssuePriorityException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
