package com.fourttttty.corookie.plan.application.repository;

import com.fourttttty.corookie.plan.domain.Plan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    List<Plan> findByProjectIdAndDate(Long projectId, LocalDate date);
    Optional<Plan> findById(Long id);

    Plan save(Plan plan);

}
