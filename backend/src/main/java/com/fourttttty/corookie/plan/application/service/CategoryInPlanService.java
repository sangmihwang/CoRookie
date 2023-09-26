package com.fourttttty.corookie.plan.application.service;

import com.fourttttty.corookie.plan.application.repository.CategoryInPlanRepository;
import com.fourttttty.corookie.plan.application.repository.PlanCategoryRepository;
import com.fourttttty.corookie.plan.application.repository.PlanRepository;
import com.fourttttty.corookie.plan.domain.CategoryInPlan;
import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.dto.request.PlanCategoryCreateRequest;
import com.fourttttty.corookie.plan.dto.response.CalendarPlanResponse;
import com.fourttttty.corookie.plan.dto.response.PlanCategoryResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryInPlanService {

    private final PlanRepository planRepository;
    private final CategoryInPlanRepository categoryInPlanRepository;
    private final PlanCategoryRepository planCategoryRepository;

    @Transactional
    public PlanCategoryResponse create(Long planId, PlanCategoryCreateRequest request) {
        return PlanCategoryResponse.from(categoryInPlanRepository.save(
                CategoryInPlan.of(
                        planRepository.findById(planId).orElseThrow(EntityNotFoundException::new),
                        planCategoryRepository.findByContent(request.content()).orElseThrow(EntityNotFoundException::new))));
    }

    public List<PlanCategoryResponse> findAllByPlanId(Long planId) {
        return categoryInPlanRepository.findByPlanId(planId).stream()
                .map(PlanCategoryResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(Long categoryInPlanId) {
        categoryInPlanRepository.deleteById(categoryInPlanId);
    }
}
