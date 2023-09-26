package com.fourttttty.corookie.issue.dto.request;

import jakarta.validation.constraints.NotNull;

public record IssueManagerUpdateRequest(@NotNull Long managerId) {
}
