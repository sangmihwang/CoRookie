package com.fourttttty.corookie.plan.dto.request;

import com.fourttttty.corookie.plan.domain.PlanCategory;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PlanCategoryCreateRequest(@NotEmpty String content,
                                        @NotBlank String color) {
    public PlanCategory toEntity(Project project) {
        return PlanCategory.of(content, color, project);
    }
}
