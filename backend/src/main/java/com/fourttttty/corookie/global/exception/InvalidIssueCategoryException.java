package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class InvalidIssueCategoryException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public InvalidIssueCategoryException() {
        this(ExceptionCode.ISSUE_CATEGORY_INVALID);
    }

    public InvalidIssueCategoryException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
