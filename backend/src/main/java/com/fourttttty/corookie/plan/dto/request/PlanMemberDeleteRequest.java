package com.fourttttty.corookie.plan.dto.request;

import jakarta.validation.constraints.NotNull;

public record PlanMemberDeleteRequest(@NotNull Long memberId) {
}
