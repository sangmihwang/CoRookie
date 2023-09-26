package com.fourttttty.corookie.plan.application.repository;

import com.fourttttty.corookie.plan.domain.PlanCategory;

import java.util.List;
import java.util.Optional;

public interface PlanCategoryRepository {
    PlanCategory save(PlanCategory planCategory);

    List<PlanCategory> findByProjectId(Long projectId);
    Optional<PlanCategory> findById(Long id);
    Optional<PlanCategory> findByContent(String content);
    void deleteById(Long planCategoryId);
}
