package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class ProjectNotOpenForInvitationException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public ProjectNotOpenForInvitationException() {
        this(ExceptionCode.PROJECT_NOT_OPEN_FOR_INVITATION);
    }

    public ProjectNotOpenForInvitationException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
