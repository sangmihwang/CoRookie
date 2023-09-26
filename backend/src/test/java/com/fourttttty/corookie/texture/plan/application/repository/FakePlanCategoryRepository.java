package com.fourttttty.corookie.texture.plan.application.repository;

import com.fourttttty.corookie.plan.application.repository.PlanCategoryRepository;
import com.fourttttty.corookie.plan.domain.PlanCategory;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public class FakePlanCategoryRepository implements PlanCategoryRepository {

    private long autoIncrementId = 1L;
    private final Map<Long, PlanCategory> store = new HashMap<>();

    @Override
    public PlanCategory save(PlanCategory planCategory) {
        store.put(autoIncrementId++, planCategory);
        return planCategory;
    }

    @Override
    public List<PlanCategory> findByProjectId(Long projectId) {
        return store.values().stream()
                .filter(planCategory -> planCategory.getProject().getId().equals(projectId))
                .toList();
    }

    @Override
    public Optional<PlanCategory> findById(Long id) {
        return Optional.ofNullable((store.get(id)));
    }

    @Override
    public Optional<PlanCategory> findByContent(String content) {
        return store.values().stream()
            .filter(entry -> entry.getContent().equals(content))
            .findAny();
    }

    @Override
    public void deleteById(Long planCategoryId) {
        store.remove(planCategoryId);
    }
}
