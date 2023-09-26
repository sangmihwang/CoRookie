package com.fourttttty.corookie.plan.dto.response;

import com.fourttttty.corookie.plan.domain.Plan;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CalendarPlanResponse(Long id,
                                   String planName,
                                   LocalDateTime planStart,
                                   LocalDateTime planEnd,
                                   String color) {

    public static CalendarPlanResponse from(Plan plan, String color) {
        return CalendarPlanResponse.builder()
                .id(plan.getId())
                .planName(plan.getPlanName())
                .planStart(plan.getPlanStart())
                .planEnd(plan.getPlanEnd())
                .color(color)
                .build();
    }
}
