package com.fourttttty.corookie.plan.infrastructure;

import com.fourttttty.corookie.plan.domain.CategoryInPlan;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryInPlanJpaRepository extends JpaRepository<CategoryInPlan, Long> {
    List<CategoryInPlan> findAllByPlanId(Long planId);
}
