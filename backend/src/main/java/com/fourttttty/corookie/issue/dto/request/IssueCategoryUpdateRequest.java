package com.fourttttty.corookie.issue.dto.request;

import com.fourttttty.corookie.issue.domain.IssueCategory;
import jakarta.validation.constraints.NotNull;

public record IssueCategoryUpdateRequest(@NotNull IssueCategory category) {
}
