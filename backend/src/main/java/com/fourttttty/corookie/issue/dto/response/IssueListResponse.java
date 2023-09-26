package com.fourttttty.corookie.issue.dto.response;

import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import lombok.Builder;

@Builder
public record IssueListResponse(Long id,
                                String topic,
                                IssueProgress progress,
                                IssuePriority priority,
                                IssueCategory category,
                                String memberName,
                                String memberImageUrl) {

    public static IssueListResponse from(Issue issue) {
        return IssueListResponse.builder()
                .id(issue.getId())
                .topic(issue.getTopic())
                .progress(issue.getProgress())
                .priority(issue.getPriority())
                .category(issue.getCategory())
                .memberName(issue.getManager().getName())
                .memberImageUrl(issue.getManager().getImageUrl())
                .build();
    }
}
