package com.fourttttty.corookie.plan.application.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.plan.domain.PlanCategory;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
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
public class PlanCategoryRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PlanCategoryRepository planCategoryRepository;

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

    @BeforeEach
    void initObjects() {
        memberRepository.save(member);
        projectRepository.save(project);
    }

    @Test
    @DisplayName("일정 카테고리를 저장한다.")
    void save() {
        // given
        PlanCategory planCategory = PlanCategory.of("testCategory1", "#ffddaa", project);

        // when
        PlanCategory savedCategory = planCategoryRepository.save(planCategory);

        //then
        assertThat(savedCategory).isEqualTo(planCategory);
    }

    @Test
    @DisplayName("일정 카테고리Id를 통해 조회한다.")
    void findById() {
        // given
        PlanCategory planCategory = PlanCategory.of("testCategory1", "#ffddaa", project);
        planCategoryRepository.save(planCategory);

        // when
        Optional<PlanCategory> foundCategory = planCategoryRepository.findById(planCategory.getId());

        //then
        assertThat(foundCategory).isNotEmpty();
        assertThat(foundCategory).get().isEqualTo(planCategory);
    }

    @Test
    @DisplayName("일정 카테고리 내용을 통해 조회한다.")
    void findByContent() {
        // given
        PlanCategory planCategory = PlanCategory.of("testCategory1", "#ffddaa", project);
        planCategoryRepository.save(planCategory);

        // when
        Optional<PlanCategory> foundCategory = planCategoryRepository.findByContent("testCategory1");

        //then
        assertThat(foundCategory).isNotEmpty();
        assertThat(foundCategory).get().isEqualTo(planCategory);
        assertThat(foundCategory.get().getContent()).isEqualTo(planCategory.getContent());
    }
}
