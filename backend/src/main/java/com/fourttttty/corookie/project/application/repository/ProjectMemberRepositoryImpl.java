package com.fourttttty.corookie.project.application.repository;

import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.domain.ProjectMemberId;
import com.fourttttty.corookie.project.infrastructure.ProjectMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectMemberRepositoryImpl implements ProjectMemberRepository {
    private final ProjectMemberJpaRepository projectMemberJpaRepository;

    @Override
    public Optional<ProjectMember> findById(ProjectMemberId id) {
        return projectMemberJpaRepository.findById(id);
    }

    @Override
    public List<ProjectMember> findByMemberId(Long memberId) {
        return projectMemberJpaRepository.findAllByIdMemberId(memberId);
    }

    @Override
    public List<ProjectMember> findByProjectId(Long projectId) {
        return projectMemberJpaRepository.findAllByIdProjectId(projectId);
    }

    @Override
    public boolean existsMemberInProject(Long projectId, Long memberId) {
        return projectMemberJpaRepository.existsByIdProjectIdAndIdMemberId(projectId, memberId);
    }

    @Override
    public void deleteById(ProjectMemberId id) {
        projectMemberJpaRepository.deleteById(id);
    }

    @Override
    public Long countByProjectId(Long projectId) {
        return projectMemberJpaRepository.countByIdProjectId(projectId);
    }

    @Override
    public ProjectMember save(ProjectMember projectMember) {
        return projectMemberJpaRepository.save(projectMember);
    }
}

