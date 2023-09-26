package com.fourttttty.corookie.plan.application.repository;

import com.fourttttty.corookie.plan.domain.PlanCategory;
import com.fourttttty.corookie.plan.infrastructure.PlanCategoryJpaRepository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanCategoryRepositoryImpl implements PlanCategoryRepository {

    private final PlanCategoryJpaRepository planCategoryJpaRepository;

    @Override
    public PlanCategory save(PlanCategory planCategory) {
        return planCategoryJpaRepository.save(planCategory);
    }

    @Override
    public List<PlanCategory> findByProjectId(Long projectId) {
        return planCategoryJpaRepository.findByProjectId(projectId);
    }

    @Override
    public Optional<PlanCategory> findById(Long id) {
        return planCategoryJpaRepository.findById(id);
    }

    @Override
    public Optional<PlanCategory> findByContent(String content) {
        return planCategoryJpaRepository.findByContent(content);
    }

    @Override
    public void deleteById(Long planCategoryId) {
        planCategoryJpaRepository.deleteById(planCategoryId);
    }
}
