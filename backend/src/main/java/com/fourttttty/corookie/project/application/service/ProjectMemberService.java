package com.fourttttty.corookie.project.application.service;

import com.fourttttty.corookie.directmessagechannel.application.service.DirectMessageChannelService;
import com.fourttttty.corookie.global.exception.ProjectNotFoundException;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectMemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.domain.ProjectMemberId;
import com.fourttttty.corookie.project.dto.request.ProjectMemberCreateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectMemberResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final DirectMessageChannelService directMessageChannelService;

    @Transactional
    public ProjectMemberResponse create(ProjectMemberCreateRequest request) {
        ProjectMember projectMember = projectMemberRepository.save(ProjectMember.of(
                projectRepository.findById(request.projectId()).orElseThrow(EntityNotFoundException::new),
                memberRepository.findById(request.memberId()).orElseThrow(EntityNotFoundException::new)));

        generateDirectMessageChannel(projectMemberRepository.findByProjectId(request.projectId()), projectMember);
        return ProjectMemberResponse.from(projectMember);
    }

    private void generateDirectMessageChannel(List<ProjectMember> existingProjectMembers, ProjectMember projectMember) {
        existingProjectMembers.forEach(pm -> directMessageChannelService.save(
                pm.getId().getMember(),
                projectMember.getId().getMember(),
                projectMember.getId().getProject()));
    }

    @Transactional
    public void delete(Long projectId, Long memberId) {
        projectMemberRepository.deleteById(new ProjectMemberId(
                projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new),
                memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new)));

        deleteIfNotExistsMember(projectId);
    }

    private void deleteIfNotExistsMember(Long projectId) {
        if (notExistsProjectMember(projectId)) {
            Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
            project.delete();
        }
    }

    private boolean notExistsProjectMember(Long projectId) {
        return projectMemberRepository.countByProjectId(projectId) <= 0;
    }

    public List<ProjectMemberResponse> findByProjectId(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(projectMember -> ProjectMemberResponse.from(
                        ProjectMember.of(projectMember.getId().getProject(), projectMember.getId().getMember())))
                .toList();
    }

    public List<ProjectMemberResponse> findByMemberId(Long memberId) {
        return projectMemberRepository.findByMemberId(memberId).stream()
                .map(projectMember -> ProjectMemberResponse.from(
                        ProjectMember.of(projectMember.getId().getProject(), projectMember.getId().getMember())))
                .toList();
    }

    public ProjectMemberResponse findById(ProjectMemberId projectMemberId) {
        return ProjectMemberResponse.from(projectMemberRepository.findById(projectMemberId)
                .orElseThrow(EntityNotFoundException::new));
    }

    public Boolean existsProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.existsMemberInProject(projectId, memberId);
    }
}
