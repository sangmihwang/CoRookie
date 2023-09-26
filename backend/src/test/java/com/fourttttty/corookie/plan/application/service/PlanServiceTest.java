package com.fourttttty.corookie.plan.application.service;

import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.plan.application.repository.CategoryInPlanRepository;
import com.fourttttty.corookie.plan.application.repository.PlanCategoryRepository;
import com.fourttttty.corookie.plan.application.repository.PlanMemberRepository;
import com.fourttttty.corookie.plan.application.repository.PlanRepository;
import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.domain.PlanCategory;
import com.fourttttty.corookie.plan.dto.request.PlanCategoryCreateRequest;
import com.fourttttty.corookie.plan.dto.request.PlanCreateRequest;
import com.fourttttty.corookie.plan.dto.request.PlanMemberCreateRequest;
import com.fourttttty.corookie.plan.dto.request.PlanUpdateRequest;
import com.fourttttty.corookie.plan.dto.response.PlanResponse;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.texture.member.application.repository.FakeMemberRepository;
import com.fourttttty.corookie.texture.plan.application.repository.FakeCategoryInPlanRepository;
import com.fourttttty.corookie.texture.plan.application.repository.FakePlanCategoryRepository;
import com.fourttttty.corookie.texture.plan.application.repository.FakePlanMemberRepository;
import com.fourttttty.corookie.texture.plan.application.repository.FakePlanRepository;
import com.fourttttty.corookie.texture.project.application.repository.FakeProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanServiceTest {

    PlanRepository planRepository;
    PlanCategoryRepository planCategoryRepository;
    CategoryInPlanRepository categoryInPlanRepository;
    MemberRepository memberRepository;
    ProjectRepository projectRepository;
    PlanMemberRepository planMemberRepository;

    PlanService planService;
    PlanCategoryService planCategoryService;
    CategoryInPlanService categoryInPlanService;
    PlanMemberService planMemberService;

    private Member member;
    private Project project;
    private Plan plan;
    private PlanCategory planCategory;


    @BeforeEach
    void initObjects() {
        planRepository = new FakePlanRepository();
        categoryInPlanRepository = new FakeCategoryInPlanRepository();
        planCategoryRepository = new FakePlanCategoryRepository();
        memberRepository = new FakeMemberRepository();
        projectRepository = new FakeProjectRepository();
        planMemberRepository = new FakePlanMemberRepository(planRepository);

        planCategoryService = new PlanCategoryService(planCategoryRepository, projectRepository);
        categoryInPlanService = new CategoryInPlanService(planRepository, categoryInPlanRepository, planCategoryRepository);
        planMemberService = new PlanMemberService(planMemberRepository,memberRepository, planRepository);
        planService = new PlanService(planRepository, projectRepository, planCategoryRepository, memberRepository,
                categoryInPlanService, planMemberService);

        member = Member.of( "memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName", "description", true,
            "http://test.com", false, member);
        plan = Plan.of("name", "description", LocalDateTime.now(), LocalDateTime.now(), true, project);
        planCategory = PlanCategory.of("content", "#ffddaa", project);
        memberRepository.save(member);
        projectRepository.save(project);
        planCategoryRepository.save(planCategory);
    }

    @Test
    @DisplayName("일정 생성")
    void createPlan() {
        // given
        PlanCategoryCreateRequest categoryCreateRequest = new PlanCategoryCreateRequest("CreateCategory", "#ffddaa");
        planCategoryService.create(categoryCreateRequest, project.getId());
        PlanCreateRequest request = new PlanCreateRequest("createPlan",
                "createPlanDescription",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now(),
                List.of(1L),
                List.of(1L));

        // when
        PlanResponse response = planService.createPlan(request, project.getId());

        // then
        assertThat(response.planName()).isEqualTo(request.planName());
        assertThat(response.description()).isEqualTo(request.description());
        assertThat(response.planStart()).isEqualTo(request.planStart());
        assertThat(response.planEnd()).isEqualTo(request.planEnd());
    }

    @Test
    @DisplayName("일정 조회")
    void findById() {
        // given
        planRepository.save(plan);

        // when
        PlanResponse response = planService.findById(plan.getId());

        // then
        assertThat(plan.getPlanName()).isEqualTo(response.planName());
        assertThat(plan.getDescription()).isEqualTo(response.description());
        assertThat(plan.getPlanStart()).isEqualTo(response.planStart());
        assertThat(plan.getPlanEnd()).isEqualTo(response.planEnd());
    }

    @Test
    @DisplayName("일정 수정")
    void modifyPlan() {
        // given
        planRepository.save(plan);

        PlanUpdateRequest updateRequest = new PlanUpdateRequest("modifyPlan",
            "modifyPlanDescription",
            LocalDateTime.now().minusDays(4),
            LocalDateTime.now().minusDays(2));

        // when
        PlanResponse response = planService.modifyPlan(updateRequest, plan.getId());

        // then
        assertThat(response.planName()).isEqualTo(updateRequest.planName());
        assertThat(response.description()).isEqualTo(updateRequest.description());
    }

    @Test
    @DisplayName("일정 삭제")
    void deletePlan() {
        // given
        planRepository.save(plan);

        //when
        planService.deletePlan(plan.getId());

        //then
        assertThat(planService.findById(plan.getId()).enabled()).isEqualTo(false);
    }
}