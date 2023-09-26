package com.fourttttty.corookie.texture.issue.application.repository;

import com.fourttttty.corookie.global.exception.ProjectNotFoundException;
import com.fourttttty.corookie.issue.application.repository.IssueRepository;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;

public class FakeIssueRepository implements IssueRepository {
    private long autoIncrementId = 1L;
    private final Map<Long, Issue> store = new HashMap<>();
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public FakeIssueRepository(ProjectRepository projectRepository, MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Issue save(Issue issue) {
        store.put(autoIncrementId++, issue);
        return issue;
    }

    @Override
    public Optional<Issue> findById(Long issueId) {
        return Optional.ofNullable(store.get(issueId));
    }

    @Override
    public List<Issue> findByProjectId(Long projectId) {
        return store.entrySet().stream()
                .filter(entry -> entry.getValue().getProject()
                        .equals(projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new)))
                .map(entry -> store.get(entry.getKey()))
                .toList();
    }

    @Override
    public void deleteById(Long issueId) {
        store.remove(issueId);
    }

    @Override
    public List<Issue> findByManager(Long projectId, Long managerId) {
        return store.values().stream()
                .filter(issue -> issue.getProject().equals(projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new)))
                .filter(issue -> issue.getManager().equals(memberRepository.findById(managerId).orElseThrow(EntityNotFoundException::new)))
                .toList();
    }

    @Override
    public List<Issue> findLikeTopic(Long projectId, String topic) {
        return store.values().stream()
                .filter(issue -> issue.getTopic().contains(topic))
                .toList();
    }

    @Override
    public List<Issue> findOrderByPriorityAsc(Long projectId) {
        return store.values().stream()
                .sorted(Comparator.comparingInt(issue -> issue.getPriority().getValue()))
                .toList();
    }

    @Override
    public List<Issue> findOrderByPriorityDesc(Long projectId) {
        return  store.values().stream()
                .sorted(Comparator.comparingInt(issue -> -issue.getPriority().getValue()))
                .toList();
    }

    @Override
    public List<Issue> findByProgress(Long projectId, IssueProgress progress) {
        return  store.values().stream()
                .filter(issue -> issue.getProgress().equals(progress))
                .toList();
    }

    @Override
    public List<Issue> findByCategory(Long projectId, IssueCategory category) {
        return store.values().stream()
                .filter(issue -> issue.getCategory().equals(category))
                .toList();
    }
}
