package com.fourttttty.corookie.plan.application.repository;

import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.infrastructure.PlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {

    private final PlanJpaRepository planJpaRepository;

    @Override
    public List<Plan> findByProjectIdAndDate(Long projectId, LocalDate date) {
        return planJpaRepository.findAllByDate(projectId, date.getYear(), date.getMonth().getValue());
    }

    @Override
    public Optional<Plan> findById(Long id) {
        return planJpaRepository.findById(id);
    }

    @Override
    public Plan save(Plan plan) {
        return planJpaRepository.save(plan);
    }
}
