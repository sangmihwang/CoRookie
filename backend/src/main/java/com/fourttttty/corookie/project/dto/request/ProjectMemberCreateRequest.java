package com.fourttttty.corookie.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProjectMemberCreateRequest(@NotNull Long projectId,
                                         @NotNull Long memberId)  {
}
