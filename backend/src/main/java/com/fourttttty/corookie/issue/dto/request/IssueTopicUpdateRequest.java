package com.fourttttty.corookie.issue.dto.request;

import jakarta.validation.constraints.NotBlank;

public record IssueTopicUpdateRequest(@NotBlank String topic) {
}
