package com.fourttttty.corookie.texture.project.application.repository;

import com.fourttttty.corookie.global.exception.ProjectNotFoundException;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.application.repository.ProjectMemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.project.domain.ProjectMember;
import com.fourttttty.corookie.project.domain.ProjectMemberId;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;
import java.util.Map.Entry;

public class FakeProjectMemberRepository implements ProjectMemberRepository {

    private final Map<Id, ProjectMember> store = new HashMap<>();
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public FakeProjectMemberRepository(ProjectRepository projectRepository, MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    class Id {
        private Long projectId;
        private Long memberId;

        private Id(ProjectMember projectMember) {
            this.projectId = projectMember.getId().getProject().getId();
            this.memberId = projectMember.getId().getMember().getId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.projectId, this.memberId);
        }
    }

    @Override
    public Optional<ProjectMember> findById(ProjectMemberId id) {
        Project project = id.getProject();
        Member member = id.getMember();
        return store.entrySet().stream()
                .filter(entry -> entry.getValue().getId().getProject().equals(project))
                .filter(entry -> entry.getValue().getId().getMember().equals(member))
                .map(entry -> store.get(entry.getKey()))
                .findFirst();
    }

    @Override
    public List<ProjectMember> findByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        return store.entrySet().stream()
                .filter(entry -> entry.getValue().getId().getMember()
                        .equals(member))
                .map(entry -> store.get(entry.getKey()))
                .toList();
    }

    @Override
    public List<ProjectMember> findByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        return store.entrySet().stream()
                .filter(entry -> entry.getValue().getId().getProject()
                        .equals(project))
                .map(entry -> store.get(entry.getKey()))
                .toList();
    }

    @Override
    public boolean existsMemberInProject(Long projectId, Long memberId) {
        return store.values().stream()
                .anyMatch(projectMember ->
                        projectMember.getId().getProject().getId().equals(projectId) &&
                        projectMember.getId().getMember().getId().equals(memberId));
    }

    @Override
    public void deleteById(ProjectMemberId projectMemberId) {
        Project project = projectMemberId.getProject();
        Member member = projectMemberId.getMember();
        Id id = store.entrySet().stream()
                        .filter(entry -> entry.getValue().getId().getProject().equals(project))
                        .filter(entry -> entry.getValue().getId().getMember().equals(member))
                        .map(Map.Entry::getKey).findFirst().orElseThrow(EntityNotFoundException::new);
        store.remove(id);
    }

    @Override
    public Long countByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        return store.entrySet().stream()
                .filter(entry -> entry.getValue().getId().getProject().equals(project))
                .count();
    }

    @Override
    public ProjectMember save(ProjectMember projectMember) {
        Optional<Entry<Id, ProjectMember>> first = store.entrySet().stream()
                .filter(entry -> entry.getKey().equals(projectMember.getId()))
                .findFirst();
        if (first.isEmpty()) {
            Id id = new Id(projectMember);
            store.put(id, projectMember);
            return store.get(id);
        } else {
            throw new EntityExistsException();
        }
    }
}
