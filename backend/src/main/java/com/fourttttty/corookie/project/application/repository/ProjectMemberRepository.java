package com.fourttttty.corookie.project.application.repository;

import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.domain.ProjectMemberId;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository {
    Optional<ProjectMember> findById(ProjectMemberId id);
    List<ProjectMember> findByMemberId(Long memberId);
    List<ProjectMember> findByProjectId(Long projectId);
    boolean existsMemberInProject(Long projectId, Long memberId);
    void deleteById(ProjectMemberId id);
    Long countByProjectId(Long projectId);
    ProjectMember save(ProjectMember projectMember);
}
