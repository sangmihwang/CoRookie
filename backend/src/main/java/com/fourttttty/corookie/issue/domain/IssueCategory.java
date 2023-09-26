package com.fourttttty.corookie.issue.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fourttttty.corookie.global.exception.InvalidIssueCategoryException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IssueCategory {
    ALL("all"),
    BACKEND("backend"),
    FRONTEND("frontend"),
    DESIGN("design"),
    PLAN("plan"),
    ETC("etc");

    private final String value;

    @JsonCreator
    public static IssueCategory from(String value) {
        for (IssueCategory issueCategory : IssueCategory.values()) {
            if (issueCategory.getValue().equals(value)) {
                return issueCategory;
            }
        }
        throw new InvalidIssueCategoryException();
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
