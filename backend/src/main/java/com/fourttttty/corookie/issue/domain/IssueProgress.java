package com.fourttttty.corookie.issue.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fourttttty.corookie.global.exception.InvalidIssueProgressException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
public enum IssueProgress {
    TODO("todo"),
    IN_PROGRESS("inProgress"),
    DONE("done");

    private final String value;

    @JsonCreator
    public static IssueProgress from(String value) {
        for (IssueProgress issueProgress : IssueProgress.values()) {
            if (issueProgress.getValue().equals(value)) {
                return issueProgress;
            }
        }
        throw new InvalidIssueProgressException();
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
