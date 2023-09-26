package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class InvalidProjectChangeRequestException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public InvalidProjectChangeRequestException() {
        this(ExceptionCode.INVITATION_STATUS_CHANGE_REQUEST_INVALID);
    }

    public InvalidProjectChangeRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
