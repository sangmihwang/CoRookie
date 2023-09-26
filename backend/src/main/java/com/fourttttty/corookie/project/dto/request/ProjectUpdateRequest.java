package com.fourttttty.corookie.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectUpdateRequest(@NotBlank String name,
                                   @NotNull String description,
                                   @NotBlank String invitationLink,
                                   @NotNull Boolean invitationStatus) {
}
