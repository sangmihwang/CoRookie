package com.fourttttty.corookie.project.application.service;

import com.fourttttty.corookie.directmessagechannel.application.repository.DirectMessageChannelRepository;
import com.fourttttty.corookie.directmessagechannel.application.repository.DirectMessageChannelRepositoryImpl;
import com.fourttttty.corookie.directmessagechannel.application.service.DirectMessageChannelService;
import com.fourttttty.corookie.global.exception.ProjectNotFoundException;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.application.repository.ProjectMemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.dto.request.ProjectCreateRequest;
import com.fourttttty.corookie.project.dto.request.ProjectUpdateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectDetailResponse;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.project.dto.response.ProjectListResponse;
import com.fourttttty.corookie.project.util.Base62Encoder;
import com.fourttttty.corookie.textchannel.application.repository.TextChannelRepository;
import com.fourttttty.corookie.texture.directmessagechannel.application.repository.FakeDirectMessageChannelRepository;
import com.fourttttty.corookie.texture.member.application.repository.FakeMemberRepository;
import com.fourttttty.corookie.texture.project.application.repository.FakeProjectMemberRepository;
import com.fourttttty.corookie.texture.project.application.repository.FakeProjectRepository;
import com.fourttttty.corookie.texture.textchannel.application.repository.FakeTextChannelRepository;
import com.fourttttty.corookie.texture.videochannel.application.repository.FakeVideoChannelRepository;
import com.fourttttty.corookie.videochannel.application.repository.VideoChannelRepository;
import com.fourttttty.corookie.videochannel.application.repository.VideoChannelRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectServiceTest {

    ProjectRepository projectRepository;
    MemberRepository memberRepository;
    TextChannelRepository textChannelRepository;
    ProjectMemberRepository projectMemberRepository;
    VideoChannelRepository videoChannelRepository;
    DirectMessageChannelRepository directMessageChannelRepository;
    InvitationLinkGenerateService invitationLinkGenerateService;
    ProjectMemberService projectMemberService;
    ProjectService projectService;
    DirectMessageChannelService directMessageChannelService;
    private Member member;
    private Project project;

    @BeforeEach
    void initObjects() {
        projectRepository = new FakeProjectRepository();
        memberRepository = new FakeMemberRepository();
        textChannelRepository = new FakeTextChannelRepository();
        projectMemberRepository = new FakeProjectMemberRepository(projectRepository, memberRepository);
        directMessageChannelRepository = new FakeDirectMessageChannelRepository();
        videoChannelRepository = new FakeVideoChannelRepository();
        invitationLinkGenerateService = new InvitationLinkGenerateService(new Base62Encoder());
        directMessageChannelService = new DirectMessageChannelService(directMessageChannelRepository);
        projectMemberService = new ProjectMemberService(projectMemberRepository, memberRepository, projectRepository,
                directMessageChannelService);
        projectService = new ProjectService(projectRepository, textChannelRepository, videoChannelRepository,
                memberRepository, projectMemberRepository, directMessageChannelRepository, invitationLinkGenerateService,
                projectMemberService);
        member = Member.of("이름", "test@test.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName",
                "description",
                true,
                "http://test.com",
                false,
                member);
        projectRepository.save(project);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("프로젝트 생성")
    void createProject() {
        // given
        ProjectCreateRequest request = new ProjectCreateRequest("memberName", "description");

        // when
        ProjectDetailResponse response = projectService.create(request, member.getId());

        // then
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.description()).isEqualTo(request.description());
        assertThat(response.enabled()).isEqualTo(true);
        assertThat(response.invitationStatus()).isEqualTo(false);
    }

    @Test
    @DisplayName("프로젝트 상세 조회")
    void findById() {
        // when
        ProjectDetailResponse response = projectService.findById(project.getId(), member.getId());

        // then
        assertThat(response.name()).isEqualTo(project.getName());
        assertThat(response.description()).isEqualTo(project.getDescription());
        assertThat(response.enabled()).isEqualTo(project.getEnabled());
        assertThat(response.invitationStatus()).isEqualTo(project.getInvitationStatus());
    }

    @Test
    @DisplayName("프로젝트 엔티티 조회")
    void findEntityById() {
        // when
        Project foundProject = projectRepository.findById(project.getId()).orElseThrow(ProjectNotFoundException::new);

        //then
        assertThat(foundProject.getName()).isEqualTo(project.getName());
        assertThat(foundProject.getDescription()).isEqualTo(project.getDescription());
        assertThat(foundProject.getEnabled()).isEqualTo(project.getEnabled());
        assertThat(foundProject.getInvitationStatus()).isEqualTo(project.getInvitationStatus());
    }

    @Test
    @DisplayName("프로젝트 목록 조회")
    void findAll() {
        // given
        projectMemberRepository.save(ProjectMember.of(project, member));

        // when
        List<ProjectListResponse> response = projectService.findByManagerId(member.getId());

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).name()).isEqualTo(project.getName());
        assertThat(response.get(0).enabled()).isEqualTo(project.getEnabled());
    }

    @Test
    @DisplayName("프로젝트 수정")
    void modify() {
        // given
        projectMemberRepository.save(ProjectMember.of(project, member));

        ProjectUpdateRequest request = new ProjectUpdateRequest("modifiedName", "modifiedDesc",  "http://modified.com", true);

        // when
        ProjectDetailResponse response = projectService.modify(request, project.getId(), member.getId());

        // then
        assertThat(response.name()).isEqualTo("modifiedName");
        assertThat(response.description()).isEqualTo("modifiedDesc");
        assertThat(response.invitationLink()).isEqualTo("http://modified.com");
        assertThat(response.invitationStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("프로젝트 삭제")
    void delete() {
        //when
        projectService.deleteById(project.getId());

        //then
        assertThat(projectService.findById(project.getId(), member.getId()).enabled()).isEqualTo(false);
    }
}
