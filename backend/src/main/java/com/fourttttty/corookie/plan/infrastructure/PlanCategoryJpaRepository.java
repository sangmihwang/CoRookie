package com.fourttttty.corookie.plan.infrastructure;

import com.fourttttty.corookie.plan.domain.PlanCategory;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanCategoryJpaRepository extends JpaRepository<PlanCategory, Long> {
    List<PlanCategory> findByProjectId(Long projectId);
    Optional<PlanCategory> findByContent(String content);
}
