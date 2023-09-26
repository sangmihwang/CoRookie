package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class InvalidIssueFilterTypeException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public InvalidIssueFilterTypeException() {
        this(ExceptionCode.ISSUE_FILTER_TYPE_INVALID);
    }

    public InvalidIssueFilterTypeException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
