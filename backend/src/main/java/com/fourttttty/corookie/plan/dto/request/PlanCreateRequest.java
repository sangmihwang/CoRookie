package com.fourttttty.corookie.plan.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import lombok.Builder;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record PlanCreateRequest(@NotNull String planName,
                                @NotNull String description,
                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                @NotNull LocalDateTime planStart,
                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                @NotNull LocalDateTime planEnd,
                                @NotNull List<Long> categoryIds,
                                @NotNull List<Long> memberIds) {
}
