package com.fourttttty.corookie.project.dto.response;

import com.fourttttty.corookie.project.domain.Project;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectListResponse(Long id,
                                  String name,
                                  String description,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  Boolean enabled) {
    public static ProjectListResponse from(Project project) {
        return ProjectListResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .enabled(project.getEnabled())
                .build();
    }
}
