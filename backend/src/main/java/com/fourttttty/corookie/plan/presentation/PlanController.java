package com.fourttttty.corookie.plan.presentation;

import com.fourttttty.corookie.plan.application.service.PlanService;
import com.fourttttty.corookie.plan.dto.request.PlanCreateRequest;

import com.fourttttty.corookie.plan.dto.request.PlanMemberCreateRequest;
import com.fourttttty.corookie.plan.dto.request.PlanMemberDeleteRequest;
import com.fourttttty.corookie.plan.dto.request.PlanUpdateRequest;
import com.fourttttty.corookie.plan.dto.response.CalendarPlanResponse;
import com.fourttttty.corookie.plan.dto.response.PlanMemberResponse;
import com.fourttttty.corookie.plan.dto.response.PlanResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectId}/plans")
public class PlanController {
    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<CalendarPlanResponse>> planCalendarList(@PathVariable Long projectId,
                                                                       @RequestParam
                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                       LocalDate date) {
        return ResponseEntity.ok(planService.findByProjectIdAndDate(projectId, date));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponse> planDetail(@PathVariable Long planId) {
        return ResponseEntity.ok(planService.findById(planId));
    }

    @PostMapping
    public ResponseEntity<PlanResponse> planCreate(@PathVariable Long projectId,
                                                   @RequestBody @Validated PlanCreateRequest request) {
        return ResponseEntity.ok(planService.createPlan(request, projectId));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<PlanResponse> planModify(@PathVariable Long planId,
                                                   @RequestBody @Validated PlanUpdateRequest request) {
        return ResponseEntity.ok(planService.modifyPlan(request, planId));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Object> planDelete(@PathVariable Long planId) {
        planService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/members")
    public ResponseEntity<PlanMemberResponse> memberCreate(@PathVariable Long planId,
                                                           @RequestBody @Validated PlanMemberCreateRequest request) {
        return ResponseEntity.ok(planService.createPlanMember(planId, request));
    }

    @DeleteMapping("/{planId}/members")
    public ResponseEntity<Object> memberDelete(@PathVariable Long planId,
                                               @RequestBody @Validated PlanMemberDeleteRequest request) {
        planService.deletePlanMember(planId, request);
        return ResponseEntity.noContent().build();
    }
}
