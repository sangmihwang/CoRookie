package com.fourttttty.corookie.plan.dto.response;

import com.fourttttty.corookie.plan.domain.Plan;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record PlanResponse(Long planId,
                           String planName,
                           String description,
                           LocalDateTime planStart,
                           LocalDateTime planEnd,
                           List<PlanCategoryResponse> categories,
                           List<PlanMemberResponse> members,
                           Boolean enabled) {
    public static PlanResponse from(Plan plan, List<PlanCategoryResponse> categories, List<PlanMemberResponse> members) {
        return PlanResponse.builder()
            .planId(plan.getId())
            .planName(plan.getPlanName())
            .description(plan.getDescription())
            .planStart(plan.getPlanStart())
            .planEnd(plan.getPlanEnd())
            .categories(categories)
            .members(members)
            .enabled(plan.getEnabled())
            .build();
    }
}