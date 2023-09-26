package com.fourttttty.corookie.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberNameUpdateRequest(@NotBlank String name) {
}
