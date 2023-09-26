package com.fourttttty.corookie.issue.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fourttttty.corookie.global.exception.InvalidIssueFilterTypeException;

public enum IssueFilterType {
    MANAGER("manager"),
    TOPIC("topic"),
    PROGRESS("progress"),
    CATEGORY("category"),
    PRIORITY("priority");
    private final String value;

    IssueFilterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static IssueFilterType from(String value) {
        for (IssueFilterType type : IssueFilterType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        throw new InvalidIssueFilterTypeException();
    }

}
