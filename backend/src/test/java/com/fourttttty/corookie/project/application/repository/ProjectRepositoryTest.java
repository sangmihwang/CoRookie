package com.fourttttty.corookie.project.application.repository;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
import com.fourttttty.corookie.texture.project.application.repository.FakeProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
class ProjectRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    MemberRepository memberRepository;
    private Member member;

    @BeforeEach
    void initObjects() {
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        memberRepository.save(member);
    }

    @Test
    @DisplayName("프로젝트 저장 테스트")
    void saveProject() {
        // given
        Project project = Project.of("memberName",
                "description",
                Boolean.FALSE,
                "http://test.com",
                Boolean.FALSE,
                member);

        // when
        Project savedProject = projectRepository.save(project);

        // then
        assertThat(savedProject.getName()).isEqualTo(project.getName());
        assertThat(savedProject.getDescription()).isEqualTo(project.getDescription());
        assertThat(savedProject.getEnabled()).isEqualTo(project.getEnabled());
        assertThat(savedProject.getInvitationLink()).isEqualTo(project.getInvitationLink());
        assertThat(savedProject.getInvitationStatus()).isEqualTo(project.getInvitationStatus());
        assertThat(savedProject.getManager()).isEqualTo(project.getManager());
    }

    @Test
    @DisplayName("프로젝트 상세 조회")
    void findById() {
        // given
        Project project = Project.of("memberName",
                "description",
                Boolean.FALSE,
                "http://test.com",
                Boolean.FALSE,
                member);
        projectRepository.save(project);

        // when
        Optional<Project> foundProject = projectRepository.findById(project.getId());

        // then
        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getName()).isEqualTo(project.getName());
        assertThat(foundProject.get().getDescription()).isEqualTo(project.getDescription());
        assertThat(foundProject.get().getEnabled()).isEqualTo(project.getEnabled());
        assertThat(foundProject.get().getInvitationLink()).isEqualTo(project.getInvitationLink());
        assertThat(foundProject.get().getInvitationStatus()).isEqualTo(project.getInvitationStatus());
        assertThat(foundProject.get().getManager()).isEqualTo(project.getManager());
    }

// To-Do : findByInvitaion Link 테스트 짜기    findByInvitationLink

    @Test
    @DisplayName("프로젝트 목록 조회")
    void findAll() {
        // given
        Project project = Project.of("memberName",
                "description",
                Boolean.FALSE,
                "http://test.com",
                Boolean.FALSE,
                member);
        projectRepository.save(project);

        // when
        List<Project> projects = projectRepository.findAll();

        // then
        assertThat(projects.size()).isEqualTo(1);
        assertThat(projects.get(0).getName()).isEqualTo(project.getName());
        assertThat(projects.get(0).getDescription()).isEqualTo(project.getDescription());
        assertThat(projects.get(0).getEnabled()).isEqualTo(project.getEnabled());
        assertThat(projects.get(0).getInvitationLink()).isEqualTo(project.getInvitationLink());
        assertThat(projects.get(0).getInvitationStatus()).isEqualTo(project.getInvitationStatus());
        assertThat(projects.get(0).getManager()).isEqualTo(project.getManager());
    }
}

