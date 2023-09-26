package com.fourttttty.corookie.plan.application.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.plan.domain.CategoryInPlan;
import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.domain.PlanCategory;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
public class CategoryInPlanRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanCategoryRepository planCategoryRepository;
    @Autowired
    private CategoryInPlanRepository categoryInPlanRepository;


    Member member = Member.of(
            "memberName",
            "memberEmail",
            "https://test",
            Oauth2.of(AuthProvider.KAKAO, "account"));
    Project project = Project.of("memberName",
        "description",
        true,
        "http://test.com",
        false,
        member);
    Plan plan = Plan.of("testPlan",
        "planDescription",
        LocalDateTime.now().minusDays(2),
        LocalDateTime.now(),
        true,
        project
    );

    PlanCategory planCategory = PlanCategory.of("content", "#ffddaa", project);

    @BeforeEach
    void setUp() {
        memberRepository.save(member);
        projectRepository.save(project);
        planCategoryRepository.save(planCategory);
        planRepository.save(plan);
    }

    @Test
    @DisplayName("일정_카테고리를 저장한다.")
    void save() {
        // given
        CategoryInPlan categoryInPlan = CategoryInPlan.of(plan, planCategory);

        // when
        CategoryInPlan savedCategoryInPlan = categoryInPlanRepository.save(categoryInPlan);

        // then
        assertThat(savedCategoryInPlan).isNotNull();
        assertThat(savedCategoryInPlan.getPlan()).isEqualTo(plan);
        assertThat(savedCategoryInPlan.getPlanCategory()).isEqualTo(planCategory);
    }

    @Test
    @DisplayName("일정Id에 따른 카테고리 목록을 조회한다.")
    void findByPlan() {
        // given
        CategoryInPlan categoryInPlan = CategoryInPlan.of(plan, planCategory);
        categoryInPlanRepository.save(categoryInPlan);

        // when
        List<CategoryInPlan> foundCategoryInPlans = categoryInPlanRepository.findByPlanId(plan.getId());

        // then
        assertThat(foundCategoryInPlans).hasSize(1);
        assertThat(foundCategoryInPlans).contains(categoryInPlan);
    }

    @Test
    @DisplayName("일정_카테고리 Id에 따른 존재 여부 조회")
    void exists() {
        // given
        planCategoryRepository.save(planCategory);
        CategoryInPlan savedCategoryInPlan = categoryInPlanRepository.save(CategoryInPlan.of(plan, planCategory));

        // when
        Boolean exist = categoryInPlanRepository.exists(savedCategoryInPlan.getId());

        // then
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("일정_카테고리를 삭제한다.")
    void delete() {
        // given
        CategoryInPlan categoryInPlan = CategoryInPlan.of(plan, planCategory);
        categoryInPlanRepository.save(categoryInPlan);

        // when
        categoryInPlanRepository.delete(categoryInPlan);

        // then
        Boolean exist = categoryInPlanRepository.exists(categoryInPlan.getId());
        assertThat(exist).isFalse();
    }

}
