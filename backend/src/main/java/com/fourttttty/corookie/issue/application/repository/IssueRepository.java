package com.fourttttty.corookie.issue.application.repository;

import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.IssueProgress;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    Issue save(Issue issue);
    Optional<Issue> findById(Long issueId);
    List<Issue> findByProjectId(Long projectId);
    void deleteById(Long issueId);
    List<Issue> findByManager(Long projectId, Long managerId);
    List<Issue> findLikeTopic(Long projectId, String topic);
    List<Issue> findByProgress(Long projectId, IssueProgress progress);
    List<Issue> findByCategory(Long projectId, IssueCategory category);
    List<Issue> findOrderByPriorityAsc(Long projectId);
    List<Issue> findOrderByPriorityDesc(Long projectId);
}
