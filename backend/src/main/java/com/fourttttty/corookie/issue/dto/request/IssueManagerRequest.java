package com.fourttttty.corookie.issue.dto.request;

import jakarta.validation.constraints.NotBlank;

public record IssueManagerRequest(@NotBlank String memberName) {
}
