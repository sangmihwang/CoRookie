package com.fourttttty.corookie.issue.dto.request;

import com.fourttttty.corookie.issue.domain.IssuePriority;
import jakarta.validation.constraints.NotNull;

public record IssuePriorityUpdateRequest(@NotNull IssuePriority priority) {
}
