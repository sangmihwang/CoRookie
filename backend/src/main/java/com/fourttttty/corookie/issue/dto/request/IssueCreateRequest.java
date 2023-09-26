package com.fourttttty.corookie.issue.dto.request;

import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record IssueCreateRequest(@NotBlank String topic,
                                 @NotNull String description,
                                 @NotNull IssueProgress progress,
                                 @NotNull IssuePriority priority,
                                 @NotNull IssueCategory category) {

    public Issue toEntity(Project project, Member member) {
        return Issue.of(topic, description, progress,
                priority, category, true, project, member);
    }
}
