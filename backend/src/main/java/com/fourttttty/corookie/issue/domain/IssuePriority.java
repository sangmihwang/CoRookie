package com.fourttttty.corookie.issue.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fourttttty.corookie.global.exception.InvalidIssuePriorityException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IssuePriority {
    HIGHEST(5, "Highest"),
    HIGH(4, "High"),
    NORMAL(3, "Normal"),
    LOW(2, "Low"),
    LOWEST(1, "Lowest");

    private final int value;
    private final String name;

    @JsonCreator
    public static IssuePriority from(String name) {
        for (IssuePriority issuePriority : IssuePriority.values()) {
            if (issuePriority.getName().equals(name)) {
                return issuePriority;
            }
        }
        throw new InvalidIssuePriorityException();
    }

    public static IssuePriority from(int value) {
        for (IssuePriority issuePriority : IssuePriority.values()) {
            if (issuePriority.getValue() == value) {
                return issuePriority;
            }
        }
        throw new InvalidIssuePriorityException();
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }
}
