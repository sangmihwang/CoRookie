package com.fourttttty.corookie.project.dto.request;

import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;

public record ProjectCreateRequest(@NotBlank String name,
                                   @NotNull String description) {

    public Project toEntity(Member member) {
        return Project.of(name, description, true, "", false, member);
    }
}
