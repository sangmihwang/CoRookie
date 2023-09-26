package com.fourttttty.corookie.plan.dto.request;

import jakarta.validation.constraints.NotNull;

public record PlanMemberCreateRequest(@NotNull Long memberId) {
}
