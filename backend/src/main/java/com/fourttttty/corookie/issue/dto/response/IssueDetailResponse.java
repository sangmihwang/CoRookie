package com.fourttttty.corookie.issue.dto.response;

import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import lombok.Builder;

@Builder
public record IssueDetailResponse(Long id,
                                  String topic,
                                  String description,
                                  IssueProgress progress,
                                  IssuePriority priority,
                                  IssueCategory category,
                                  Long managerId,
                                  String managerName) {

    public static IssueDetailResponse from(Issue issue) {
        return IssueDetailResponse.builder()
                .id(issue.getId())
                .topic(issue.getTopic())
                .description(issue.getDescription())
                .progress(issue.getProgress())
                .priority(issue.getPriority())
                .category(issue.getCategory())
                .managerId(issue.getManager().getId())
                .managerName(issue.getManager().getName())
                .build();
    }
}
