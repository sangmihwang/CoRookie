package com.fourttttty.corookie.plan.presentation;

import com.fourttttty.corookie.plan.application.service.CategoryInPlanService;
import com.fourttttty.corookie.plan.application.service.PlanCategoryService;
import com.fourttttty.corookie.plan.application.service.PlanService;
import com.fourttttty.corookie.plan.dto.request.PlanCategoryCreateRequest;
import com.fourttttty.corookie.plan.dto.response.PlanCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/plans/{planId}/categories")
@RequiredArgsConstructor
public class CategoryInPlanController {

    private final CategoryInPlanService categoryInPlanService;

    @GetMapping
    public ResponseEntity<List<PlanCategoryResponse>> categoryList(@PathVariable Long planId) {
        return ResponseEntity.ok(categoryInPlanService.findAllByPlanId(planId));
    }

    @PostMapping
    public ResponseEntity<PlanCategoryResponse> categoryCreate(@PathVariable Long planId,
                                                               @RequestBody @Validated PlanCategoryCreateRequest request) {
        return ResponseEntity.ok(categoryInPlanService.create(planId, request));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> categoryDelete(@PathVariable Long categoryId) {
        categoryInPlanService.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }

}
