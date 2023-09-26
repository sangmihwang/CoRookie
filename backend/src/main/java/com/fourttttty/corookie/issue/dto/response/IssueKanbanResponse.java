package com.fourttttty.corookie.issue.dto.response;

import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import lombok.Builder;

@Builder
public record IssueKanbanResponse(Long id,
                                  String topic,
                                  IssueProgress progress,
                                  IssuePriority priority,
                                  IssueCategory category,
                                  Long managerId) {

    public static IssueKanbanResponse from(Issue issue) {
        return IssueKanbanResponse.builder()
                .id(issue.getId())
                .topic(issue.getTopic())
                .progress(issue.getProgress())
                .priority(issue.getPriority())
                .category(issue.getCategory())
                .managerId(issue.getManager().getId())
                .build();
    }
}
