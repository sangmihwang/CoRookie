package com.fourttttty.corookie.plan.presentation;

import com.fourttttty.corookie.plan.application.service.PlanCategoryService;
import com.fourttttty.corookie.plan.dto.request.PlanCategoryCreateRequest;
import com.fourttttty.corookie.plan.dto.response.PlanCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/plan-categories")
@RequiredArgsConstructor
public class PlanCategoryController {

    private final PlanCategoryService planCategoryService;

    @GetMapping
    public ResponseEntity<List<PlanCategoryResponse>> planCategoryList(@PathVariable Long projectId) {
        return ResponseEntity.ok(planCategoryService.findByProjectId(projectId));
    }

    @PostMapping
    public ResponseEntity<PlanCategoryResponse> planCategoryCreate(@PathVariable Long projectId,
                                                                   @RequestBody @Validated PlanCategoryCreateRequest request) {
        return ResponseEntity.ok(planCategoryService.create(request, projectId));
    }

    @DeleteMapping("/{planCategoryId}")
    public ResponseEntity<Object> planCategoryDelete(@PathVariable Long planCategoryId) {
        planCategoryService.delete(planCategoryId);
        return ResponseEntity.noContent().build();
    }
}
