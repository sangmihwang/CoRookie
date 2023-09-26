package com.fourttttty.corookie.project.dto.response;

import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.project.domain.ProjectMember;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ProjectDetailResponse(Long id,
                                    String name,
                                    String description,
                                    LocalDateTime createdAt,
                                    LocalDateTime updatedAt,
                                    Boolean enabled,
                                    String invitationLink,
                                    Boolean invitationStatus,
                                    String managerName,
                                    Boolean isManager) {

    public static ProjectDetailResponse from(Project project, Boolean isManager) {
        return ProjectDetailResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .enabled(project.getEnabled())
                .invitationLink(project.getInvitationLink())
                .invitationStatus(project.getInvitationStatus())
                .managerName(project.getManager().getName())
                .isManager(isManager)
                .build();
    }
}
