package com.fourttttty.corookie.plan.application.service;

import com.fourttttty.corookie.global.exception.PlanNotFoundException;
import com.fourttttty.corookie.global.exception.ProjectNotFoundException;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.plan.application.repository.PlanCategoryRepository;
import com.fourttttty.corookie.plan.application.repository.PlanRepository;
import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.domain.PlanCategory;
import com.fourttttty.corookie.plan.domain.PlanMember;
import com.fourttttty.corookie.plan.dto.request.*;
import com.fourttttty.corookie.plan.dto.response.CalendarPlanResponse;
import com.fourttttty.corookie.plan.dto.response.PlanCategoryResponse;
import com.fourttttty.corookie.plan.dto.response.PlanMemberResponse;
import com.fourttttty.corookie.plan.dto.response.PlanResponse;

import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;
    private final ProjectRepository projectRepository;
    private final PlanCategoryRepository planCategoryRepository;
    private final MemberRepository memberRepository;
    private final CategoryInPlanService categoryInPlanService;
    private final PlanMemberService planMemberService;

    public List<CalendarPlanResponse> findByProjectIdAndDate(Long projectId, LocalDate date) {
        return planRepository.findByProjectIdAndDate(projectId, date).stream()
                .map(plan -> CalendarPlanResponse.from(plan, categoryInPlanService.findAllByPlanId(plan.getId()).stream()
                        .findFirst().orElseThrow(EntityNotFoundException::new).color()))
                .toList();
    }

    public PlanResponse findById(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(PlanNotFoundException::new);
        return PlanResponse.from(plan,
            categoryInPlanService.findAllByPlanId(id),
            planMemberService.findAllByPlanId(id));
    }

    @Transactional
    public PlanResponse createPlan(PlanCreateRequest request, Long projectId) {
        Plan plan = planRepository.save(Plan.of(request.planName(),
                request.description(),
                request.planStart(),
                request.planEnd(),
                true,
                projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new)));

        return PlanResponse.from(plan,
                request.categoryIds().stream()
                        .map(categoryId -> {
                            PlanCategory planCategory = planCategoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
                            return categoryInPlanService.create(plan.getId(), new PlanCategoryCreateRequest(planCategory.getContent(), planCategory.getColor()));
                        })
                        .toList(),
                request.memberIds().stream()
                        .map(memberId -> planMemberService.create(plan, new PlanMemberCreateRequest(memberId)))
                        .toList());
    }

    @Transactional
    public PlanResponse modifyPlan(PlanUpdateRequest request, Long planId) {
        Plan plan = findEntityById(planId);
        plan.update(request.planName(),
                request.description(),
                request.planStart(),
                request.planEnd());

        return PlanResponse.from(plan,
                categoryInPlanService.findAllByPlanId(planId),
                planMemberService.findAllByPlanId(planId));
    }

    @Transactional
    public void deletePlan(Long planId) {
        findEntityById(planId).delete();
    }

    @Transactional
    public PlanMemberResponse createPlanMember(Long id, PlanMemberCreateRequest request) {
        return planMemberService.create(findEntityById(id), request);
    }

    @Transactional
    public void deletePlanMember(Long id, PlanMemberDeleteRequest request) {
        planMemberService.deletePlanMember(PlanMember.of(
            memberRepository.findById(request.memberId()).orElseThrow(EntityNotFoundException::new),
            findEntityById(id)));
    }

    private Plan findEntityById(Long id) {
        return planRepository.findById(id).orElseThrow(PlanNotFoundException::new);
    }
}
