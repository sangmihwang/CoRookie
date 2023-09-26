package com.fourttttty.corookie.plan.application.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
class PlanRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    Member member = Member.of("memberName","memberEmail", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
    Project project = Project.of("memberName",
        "description",
        true,
        "http://test.com",
        false,
    member);

    @BeforeEach
    void setUp() {
        memberRepository.save(member);
        projectRepository.save(project);
    }

    @Test
    @DisplayName("일정을 저장한다.")
    void save() {
        // given
        Plan plan = Plan.of("testPlan",
            "planDescription",
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now(),
            true,
            project);

        // when
        Plan savedPlan = planRepository.save(plan);

        // then
        assertThat(savedPlan).isEqualTo(plan);
        assertThat(savedPlan.getPlanName()).isEqualTo(plan.getPlanName());
        assertThat(savedPlan.getDescription()).isEqualTo(plan.getDescription());
        assertThat(savedPlan.getPlanStart()).isEqualTo(plan.getPlanStart());
        assertThat(savedPlan.getPlanEnd()).isEqualTo(plan.getPlanEnd());
        assertThat(savedPlan.getEnabled()).isEqualTo(plan.getEnabled());
    }

    @Test
    @DisplayName("planId 로 일정을 조회한다.")
    void findById() {
        // given
        Plan plan = Plan.of("testPlan",
            "planDescription",
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now(),
            true,
            project);
        planRepository.save(plan);

        // when
        Optional<Plan> foundPlan = planRepository.findById(plan.getId());

        // then
        assertThat(foundPlan).isNotEmpty();
        assertThat(foundPlan).get().isEqualTo(plan);
        assertThat(foundPlan.get().getPlanName()).isEqualTo(plan.getPlanName());
        assertThat(foundPlan.get().getDescription()).isEqualTo(plan.getDescription());
        assertThat(foundPlan.get().getPlanStart()).isEqualTo(plan.getPlanStart());
        assertThat(foundPlan.get().getPlanEnd()).isEqualTo(plan.getPlanEnd());
        assertThat(foundPlan.get().getEnabled()).isEqualTo(plan.getEnabled());
    }

    @Test
    @DisplayName("년,월로 일정을 조회한다.")
    void findByDate() {
        // given
        LocalDate date = LocalDate.now();
        Plan plan = Plan.of("testPlan",
            "planDescription",
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now(),
            true,
            project);
        planRepository.save(plan);

        // when
        List<Plan> foundPlans = planRepository.findByProjectIdAndDate(project.getId(), date);

        // then
        assertThat(foundPlans).hasSize(1);
        assertThat(foundPlans.get(0).getPlanName()).isEqualTo(plan.getPlanName());
        assertThat(foundPlans.get(0).getDescription()).isEqualTo(plan.getDescription());
        assertThat(foundPlans.get(0).getPlanStart()).isEqualTo(plan.getPlanStart());
        assertThat(foundPlans.get(0).getPlanEnd()).isEqualTo(plan.getPlanEnd());
        assertThat(foundPlans.get(0).getEnabled()).isEqualTo(plan.getEnabled());
    }
}