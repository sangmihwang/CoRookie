package com.fourttttty.corookie.plan.application.repository;

import com.fourttttty.corookie.plan.domain.CategoryInPlan;

import java.util.List;

public interface CategoryInPlanRepository {

    CategoryInPlan save(CategoryInPlan categoryInPlan);

    void delete(CategoryInPlan categoryInPlan);

    List<CategoryInPlan> findByPlanId(Long planId);

    Boolean exists(Long categoryInPlanId);

    void deleteById(Long categoryInPlanId);
}
