package com.fourttttty.corookie.issue.application.service;

import com.fourttttty.corookie.global.exception.IssueNotFoundException;
import com.fourttttty.corookie.issue.application.repository.IssueRepository;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.dto.request.IssueCreateRequest;
import com.fourttttty.corookie.issue.dto.request.IssueProgressUpdateRequest;
import com.fourttttty.corookie.issue.dto.response.IssueDetailResponse;
import com.fourttttty.corookie.issue.dto.response.IssueListResponse;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public IssueDetailResponse create(IssueCreateRequest issueCreateRequest, Long projectId, Long memberId) {
        return IssueDetailResponse.from(issueRepository.save(issueCreateRequest.toEntity(
                projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new),
                memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new))));
    }

    public IssueDetailResponse findById(Long issueId) {
        return IssueDetailResponse.from(findEntityById(issueId));
    }

    public List<IssueListResponse> findByProjectId(Long projectId) {
        return issueRepository.findByProjectId(projectId).stream()
                .map(IssueListResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(Long issueId) {
        findEntityById(issueId).delete();
    }

    private Issue findEntityById(Long issueId) {
        return issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
    }
}
