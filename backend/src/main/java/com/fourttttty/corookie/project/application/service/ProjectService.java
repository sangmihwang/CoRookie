package com.fourttttty.corookie.project.application.service;

import com.fourttttty.corookie.directmessagechannel.application.repository.DirectMessageChannelRepository;
import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;
import com.fourttttty.corookie.global.exception.InvalidProjectChangeRequestException;
import com.fourttttty.corookie.global.exception.ProjectNotOpenForInvitationException;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.application.repository.ProjectMemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.dto.request.ProjectCreateRequest;
import com.fourttttty.corookie.project.dto.request.ProjectMemberCreateRequest;
import com.fourttttty.corookie.project.dto.request.ProjectUpdateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectDetailResponse;
import com.fourttttty.corookie.project.dto.response.ProjectListResponse;
import com.fourttttty.corookie.textchannel.application.repository.TextChannelRepository;
import com.fourttttty.corookie.videochannel.application.repository.VideoChannelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TextChannelRepository textChannelRepository;
    private final VideoChannelRepository videoChannelRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final DirectMessageChannelRepository directMessageChannelRepository;
    private final InvitationLinkGenerateService invitationLinkGenerateService;
    private final ProjectMemberService projectMemberService;

    public List<ProjectListResponse> findByManagerId(Long managerId) {
        return projectRepository.findByManagerId(managerId).stream()
                .map(ProjectListResponse::from)
                .toList();
    }

    public List<ProjectListResponse> findByParticipantId(Long participantId) {
        return projectMemberRepository.findByMemberId(participantId).stream()
                .map(projectMember -> ProjectListResponse.from(projectMember.getId().getProject()))
                .toList();
    }

    public ProjectDetailResponse findById(Long projectId, Long memberId) {
        Project project = findEntityById(projectId);
        return ProjectDetailResponse.from(project, project.isManager(memberId));
    }

    public Project findEntityById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public ProjectDetailResponse create(ProjectCreateRequest projectCreateRequest, Long managerId) {
        Member member = memberRepository.findById(managerId).orElseThrow(EntityNotFoundException::new);
        Project project = projectRepository.save(projectCreateRequest.toEntity(member));

        registerMemberForProject(member, project);
        project.changeInvitationLink(invitationLinkGenerateService.generateInvitationLink(project.getId()));
        project.createDefaultTextChannels().forEach(textChannelRepository::save);
        project.createDefaultVideoChannels().forEach(videoChannelRepository::save);
        return ProjectDetailResponse.from(project, project.isManager(managerId));
    }

    private void registerMemberForProject(Member member, Project project) {
        projectMemberRepository.save(ProjectMember.of(project, member));
        directMessageChannelRepository.save(DirectMessageChannel.of(true, member, member, project));
    }

    @Transactional
    public ProjectDetailResponse modify(ProjectUpdateRequest request, Long projectId, Long managerId) {
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        project.update(request.name(), request.description(), request.invitationLink(), request.invitationStatus());
        return ProjectDetailResponse.from(project, project.isManager(managerId));
    }

    @Transactional
    public void deleteById(Long projectId) {
        projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new).delete();
    }

    @Transactional
    public ProjectDetailResponse findByInvitationLink(String invitationLink, Long memberId) {
        Project project = findEntityById(invitationLinkGenerateService.decodingInvitationLink(invitationLink));
        validateInvitationStatus(project);

        projectMemberService.create(new ProjectMemberCreateRequest(project.getId(), memberId));
        return ProjectDetailResponse.from(project, project.isManager(memberId));
    }

    @Transactional
    public ProjectDetailResponse enableInvitationStatus(Long projectId, Long memberId) {
        Project project = findEntityById(projectId);
        validateManagerAuthority(memberId, project);
        project.enableLink();
        return ProjectDetailResponse.from(project, project.isManager(memberId));
    }

    @Transactional
    public ProjectDetailResponse disableInvitationStatus(Long projectId, Long memberId) {
        Project project = findEntityById(projectId);
        validateManagerAuthority(memberId, project);
        project.disableLink();
        return ProjectDetailResponse.from(project, project.isManager(memberId));
    }

    private static void validateManagerAuthority(Long memberId, Project project) {
        if (!project.isManager(memberId)) {
            throw new InvalidProjectChangeRequestException();
        }
    }

    private static void validateInvitationStatus(Project project) {
        if (!project.isEnabledLink()) {
            throw new ProjectNotOpenForInvitationException();
        }
    }
}
