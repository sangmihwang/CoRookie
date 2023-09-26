package com.fourttttty.corookie.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberProfileUpdateRequest(@NotBlank String imageUrl) {
}
