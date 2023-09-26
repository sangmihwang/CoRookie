package com.fourttttty.corookie.issue.dto.request;

import jakarta.validation.constraints.NotNull;

public record IssueDescriptionUpdateRequest(@NotNull String description) {
}
