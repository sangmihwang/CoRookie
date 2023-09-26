package com.fourttttty.corookie.project.application.service;

import com.fourttttty.corookie.directmessagechannel.application.repository.DirectMessageChannelRepository;
import com.fourttttty.corookie.directmessagechannel.application.service.DirectMessageChannelService;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.application.repository.ProjectMemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.domain.ProjectMemberId;
import com.fourttttty.corookie.project.dto.request.ProjectMemberCreateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectMemberResponse;
import com.fourttttty.corookie.project.util.Base62Encoder;
import com.fourttttty.corookie.textchannel.application.repository.TextChannelRepository;
import com.fourttttty.corookie.texture.directmessagechannel.application.repository.FakeDirectMessageChannelRepository;
import com.fourttttty.corookie.texture.member.application.repository.FakeMemberRepository;
import com.fourttttty.corookie.texture.project.application.repository.FakeProjectMemberRepository;
import com.fourttttty.corookie.texture.project.application.repository.FakeProjectRepository;
import com.fourttttty.corookie.texture.textchannel.application.repository.FakeTextChannelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProjectMemberServiceTest {

    ProjectRepository projectRepository;
    MemberRepository memberRepository;
    ProjectMemberRepository projectMemberRepository;
    TextChannelRepository textChannelRepository;
    DirectMessageChannelRepository directMessageChannelRepository;
    ProjectMemberService projectMemberService;

    private Member member;
    private Project project;

    @BeforeEach
    void initObjects() {
        projectRepository = new FakeProjectRepository();
        memberRepository = new FakeMemberRepository();
        projectMemberRepository = new FakeProjectMemberRepository(projectRepository, memberRepository);
        textChannelRepository = new FakeTextChannelRepository();
        directMessageChannelRepository = new FakeDirectMessageChannelRepository();
        projectMemberService = new ProjectMemberService(projectMemberRepository, memberRepository, projectRepository,
                new DirectMessageChannelService(directMessageChannelRepository));
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName", "description", true,
                "http://test.com", false, member);
        projectRepository.save(project);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("프로젝트-회원 관계 추가")
    void createIfNone() {
        // given
        ProjectMemberCreateRequest request = new ProjectMemberCreateRequest(project.getId(), member.getId());

        // when
        ProjectMemberResponse response = projectMemberService.create(request);

        // then
        assertThat(response.memberName()).isEqualTo(member.getName());
        assertThat(response.memberEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("프로젝트-회원 관계 삭제")
    void delete() {
        // given
        projectMemberRepository.save(ProjectMember.of(project, member));

        // when
        projectMemberService.delete(project.getId(), member.getId());

        // then
        assertThatThrownBy(() -> projectMemberService.findById(new ProjectMemberId(project, member)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("프로젝트에 참여 중인 모든 회원 조회")
    void findByProjectId() {
        // given
        projectMemberRepository.save(ProjectMember.of(project, member));

        // when
        List<ProjectMemberResponse> findResponses = projectMemberService.findByProjectId(project.getId());

        // then
        assertThat(findResponses.size()).isEqualTo(1);
        assertThat(findResponses.get(0).memberName()).isEqualTo("memberName");
        assertThat(findResponses.get(0).memberEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    @DisplayName("회원이 참여 중인 모든 프로젝트 조회")
    void findByMemberId() {
        // given
        projectMemberRepository.save(ProjectMember.of(project, member));

        // when
        List<ProjectMemberResponse> findResponses = projectMemberService.findByMemberId(member.getId());

        // then
        assertThat(findResponses.size()).isEqualTo(1);
        assertThat(findResponses.get(0).memberName()).isEqualTo("memberName");
        assertThat(findResponses.get(0).memberEmail()).isEqualTo("test@gmail.com");
    }
}
