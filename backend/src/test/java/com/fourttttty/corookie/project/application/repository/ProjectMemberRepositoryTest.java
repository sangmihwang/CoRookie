package com.fourttttty.corookie.project.application.repository;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.domain.ProjectMemberId;
import com.fourttttty.corookie.support.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
public class ProjectMemberRepositoryTest {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberRepository memberRepository;
    private Member member;
    private Member member2;
    private Project project;
    private Project project2;

    @BeforeEach
    void initObjects() {
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        member2 = Member.of("name2", "test2@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName",
                "description",
                Boolean.TRUE,
                "http://test.com",
                Boolean.FALSE,
                member);
        project2 = Project.of("name2",
                "description2",
                Boolean.TRUE,
                "http://test2.com",
                Boolean.FALSE,
                member);
        memberRepository.save(member);
        memberRepository.save(member2);
        projectRepository.save(project);
        projectRepository.save(project2);
    }

    @Test
    @DisplayName("회원으로 프로젝트 목록 조회")
    void findByMemberId() {
        // given
        List<Project> projects = List.of(project, project2);
        projectMemberRepository.save(ProjectMember.of(project, member));
        projectMemberRepository.save(ProjectMember.of(project2, member));

        // when
        List<ProjectMember> projectMembers = projectMemberRepository.findByMemberId(member.getId());

        // then
        assertThat(projects).hasSize(projectMembers.size());
        for (int i = 0; i < projectMembers.size(); i++) {
            Project foundProject = projectMembers.get(i).getId().getProject();
            assertThat(foundProject.getName()).isEqualTo(projects.get(i).getName());
            assertThat(foundProject.getDescription()).isEqualTo(projects.get(i).getDescription());
            assertThat(foundProject.getEnabled()).isEqualTo(projects.get(i).getEnabled());
            assertThat(foundProject.getInvitationLink()).isEqualTo(projects.get(i).getInvitationLink());
            assertThat(foundProject.getInvitationStatus()).isEqualTo(projects.get(i).getInvitationStatus());
            assertThat(foundProject.getManager()).isEqualTo(projects.get(i).getManager());
        }
    }

    @Test
    @DisplayName("프로젝트의 회원 목록 조회")
    void findByProjectId() {
        // given
        List<Member> members = List.of(member, member2);
        projectMemberRepository.save(ProjectMember.of(project, member));
        projectMemberRepository.save(ProjectMember.of(project, member2));

        // when
        List<ProjectMember> projectMembers = projectMemberRepository.findByProjectId(project.getId());

        // then
        assertThat(members).hasSize(projectMembers.size());
        for (int i = 0; i < projectMembers.size(); i++) {
            Member foundMember = projectMembers.get(i).getId().getMember();
            assertThat(foundMember.getName()).isEqualTo(members.get(i).getName());
            assertThat(foundMember.getEmail()).isEqualTo(members.get(i).getEmail());
        }
    }

    @Test
    @DisplayName("프로젝트에서 회원 삭제")
    void deleteByProjectAndMember() {
        // given
        projectMemberRepository.save(ProjectMember.of(project, member));
        ProjectMemberId id = new ProjectMemberId(project, member);

        //when
        projectMemberRepository.deleteById(id);
        List<ProjectMember> list1 = projectMemberRepository.findByMemberId(member.getId());
        List<ProjectMember> list2 = projectMemberRepository.findByProjectId(project.getId());

        //then
        assertThat(list1).hasSize(0);
        assertThat(list2).hasSize(0);
    }

    @Test
    @DisplayName("프로젝트에 참여 중인 회원 카운트를 조회한다.")
    void countByProject() {
        // given
        List<Member> members = List.of(member, member2);
        projectMemberRepository.save(ProjectMember.of(project, member));
        projectMemberRepository.save(ProjectMember.of(project, member2));

        // when
        Long count = projectMemberRepository.countByProjectId(project.getId());

        // then
        assertThat(count).isEqualTo(members.size());
    }

    @Test
    @DisplayName("프로젝트에 회원 등록")
    void save() {
        // given
        ProjectMember projectMember = ProjectMember.of(project, member);
        projectMemberRepository.save(projectMember);

        // when
        ProjectMember foundProjectMember1 = projectMemberRepository.findByMemberId(member.getId()).get(0);
        ProjectMember foundProjectMember2 = projectMemberRepository.findByProjectId(project.getId()).get(0);

        // then
        assertThat(foundProjectMember1).isEqualTo(foundProjectMember2);
        assertThat(foundProjectMember1.getId().getProject()).isEqualTo(project);
        assertThat(foundProjectMember1.getId().getMember()).isEqualTo(member);
    }
}
