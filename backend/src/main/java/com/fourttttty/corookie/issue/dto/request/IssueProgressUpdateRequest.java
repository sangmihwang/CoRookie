package com.fourttttty.corookie.issue.dto.request;

import com.fourttttty.corookie.issue.domain.IssueProgress;
import jakarta.validation.constraints.NotNull;

public record IssueProgressUpdateRequest(@NotNull IssueProgress progress) {
}
