package com.fourttttty.corookie.texture.plan.application.repository;

import com.fourttttty.corookie.plan.application.repository.CategoryInPlanRepository;
import com.fourttttty.corookie.plan.application.repository.PlanRepository;
import com.fourttttty.corookie.plan.domain.CategoryInPlan;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeCategoryInPlanRepository implements CategoryInPlanRepository {

    private Long autoIncrementId = 1L;
    private final Map<Long, CategoryInPlan> store = new HashMap<>();

    @Override
    public CategoryInPlan save(CategoryInPlan categoryInPlan) {
        store.put(autoIncrementId++, categoryInPlan);
        return categoryInPlan;
    }

    @Override
    public void delete(CategoryInPlan categoryInPlan) {
        store.remove(categoryInPlan.getId());
    }

    @Override
    public List<CategoryInPlan> findByPlanId(Long planId) {
        return store.values().stream()
            .filter(categoryInPlan -> categoryInPlan.getPlan().getId().equals(planId))
            .toList();
    }


    @Override
    public Boolean exists(Long categoryInPlanId) {
        return store.containsKey(categoryInPlanId);
    }

    @Override
    public void deleteById(Long categoryInPlanId) {
        store.remove(categoryInPlanId);
    }
}
