package com.fourttttty.corookie.issue.application.service;

import com.fourttttty.corookie.issue.application.repository.IssueRepository;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.issue.dto.response.IssueListResponse;
import com.fourttttty.corookie.issue.util.IssueFilterType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IssueFilteringService {
    public static final String DESC = "desc";
    private final IssueRepository issueRepository;

    public List<IssueListResponse> findByFiltering(Long projectId, IssueFilterType type, String condition) {
        if (IssueFilterType.MANAGER.equals(type)) {
            return findByManager(projectId, Long.parseLong(condition));
        }
        if (IssueFilterType.TOPIC.equals(type)) {
            return findByFilteringWithTopic(projectId, condition);
        }
        if (IssueFilterType.PROGRESS.equals(type)) {
            return findByFilteringWithProgress(projectId, IssueProgress.from(condition));
        }
        if (IssueFilterType.CATEGORY.equals(type)) {
            return findByFilteringWithCategory(projectId, IssueCategory.from(condition));
        }
        if (IssueFilterType.PRIORITY.equals(type)) {
            return findOrderByPriority(projectId, condition);
        }
        return List.of();
    }

    private List<IssueListResponse> findOrderByPriority(Long projectId, String sorted) {
        if (sorted.equals(DESC)) {
            return findOrderByPriorityDesc(projectId);
        }
        return findOrderByPriorityAsc(projectId);
    }

    public List<IssueListResponse> findByManager(Long projectId, Long managerId) {
        return issueRepository.findByManager(projectId, managerId).stream()
                .map(IssueListResponse::from)
                .toList();
    }

    public List<IssueListResponse> findByFilteringWithTopic(Long projectId, String topic) {
        return issueRepository.findLikeTopic(projectId, topic).stream()
                .map(IssueListResponse::from)
                .toList();
    }

    public List<IssueListResponse> findByFilteringWithCategory(Long projectId, IssueCategory category) {
        return issueRepository.findByCategory(projectId, category).stream()
                .map(IssueListResponse::from)
                .toList();
    }

    public List<IssueListResponse> findByFilteringWithProgress(Long projectId, IssueProgress progress) {
        return issueRepository.findByProgress(projectId, progress).stream()
                .map(IssueListResponse::from)
                .toList();
    }

    public List<IssueListResponse> findOrderByPriorityAsc(Long projectId) {
        return issueRepository.findOrderByPriorityAsc(projectId).stream()
                .map(IssueListResponse::from)
                .toList();
    }

    public List<IssueListResponse> findOrderByPriorityDesc(Long projectId) {
        return issueRepository.findOrderByPriorityDesc(projectId).stream()
                .map(IssueListResponse::from)
                .toList();
    }
}
