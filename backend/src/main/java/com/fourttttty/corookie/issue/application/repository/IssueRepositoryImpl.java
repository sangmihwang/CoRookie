package com.fourttttty.corookie.issue.application.repository;

import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.issue.infrastructure.IssueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepository {
    private final IssueJpaRepository issueJpaRepository;

    public Issue save(Issue issue) {
        return issueJpaRepository.save(issue);
    }

    public Optional<Issue> findById(Long issueId) {
        return issueJpaRepository.findById(issueId);
    }

    public List<Issue> findByProjectId(Long projectId) {
        return issueJpaRepository.findByProjectId(projectId);
    }

    @Override
    public void deleteById(Long issueId) {
        issueJpaRepository.deleteById(issueId);
    }

    @Override
    public List<Issue> findByManager(Long projectId, Long managerId) {
        return issueJpaRepository.findByProjectIdAndManagerId(projectId, managerId);
    }

    @Override
    public List<Issue> findLikeTopic(Long projectId, String topic) {
        return issueJpaRepository.findByProjectIdAndTopicContaining(projectId, topic);
    }

    @Override
    public List<Issue> findOrderByPriorityAsc(Long projectId) {
        return issueJpaRepository.findAllOrderByPriorityAsc(projectId);
    }

    @Override
    public List<Issue> findOrderByPriorityDesc(Long projectId) {
        return issueJpaRepository.findAllOrderByPriorityDesc(projectId);
    }

    @Override
    public List<Issue> findByProgress(Long projectId, IssueProgress progress) {
        return issueJpaRepository.findByProjectIdAndProgress(projectId, progress);
    }

    @Override
    public List<Issue> findByCategory(Long projectId, IssueCategory category) {
        return issueJpaRepository.findByProjectIdAndCategory(projectId, category);
    }
}
