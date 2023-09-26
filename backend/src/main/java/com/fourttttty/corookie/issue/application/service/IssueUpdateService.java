package com.fourttttty.corookie.issue.application.service;

import com.fourttttty.corookie.global.exception.IssueNotFoundException;
import com.fourttttty.corookie.issue.application.repository.IssueRepository;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.dto.request.*;
import com.fourttttty.corookie.issue.dto.response.IssueDetailResponse;
import com.fourttttty.corookie.member.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueUpdateService {
    private final IssueRepository issueRepository;
    private final MemberService memberService;

    public IssueDetailResponse changeIssueTopic(Long issueId, IssueTopicUpdateRequest request) {
        Issue issue = findEntityById(issueId);
        issue.changeTopic(request.topic());

        return IssueDetailResponse.from(issue);
    }

    public IssueDetailResponse changeIssueDescription(Long issueId, IssueDescriptionUpdateRequest request) {
        Issue issue = findEntityById(issueId);
        issue.changeDescription(request.description());

        return IssueDetailResponse.from(issue);
    }

    public IssueDetailResponse changeIssueProgress(Long issueId, IssueProgressUpdateRequest request) {
        Issue issue = findEntityById(issueId);
        issue.changeProgress(request.progress());

        return IssueDetailResponse.from(issue);
    }

    public IssueDetailResponse changeIssueManager(Long issueId, IssueManagerUpdateRequest request) {
        Issue issue = findEntityById(issueId);
        issue.changeManager(memberService.findEntityById(request.managerId()));

        return IssueDetailResponse.from(issue);
    }

    public IssueDetailResponse changeIssuePriority(Long issueId, IssuePriorityUpdateRequest request) {
        Issue issue = findEntityById(issueId);
        issue.changePriority(request.priority());

        return IssueDetailResponse.from(issue);
    }

    public IssueDetailResponse changeIssueCategory(Long issueId, IssueCategoryUpdateRequest request) {
        Issue issue = findEntityById(issueId);
        issue.changeCategory(request.category());

        return IssueDetailResponse.from(issue);
    }

    private Issue findEntityById(Long issueId) {
        return issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
    }
}
