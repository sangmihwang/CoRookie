package com.fourttttty.corookie.issue.presentation;

import com.fourttttty.corookie.issue.application.service.IssueUpdateService;
import com.fourttttty.corookie.issue.dto.request.*;
import com.fourttttty.corookie.issue.dto.response.IssueDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/issues")
@RequiredArgsConstructor
public class IssueModifyController {
    private final IssueUpdateService issueUpdateService;

    @PutMapping("/{issueId}/topic")
    public ResponseEntity<IssueDetailResponse> issueTopicModify(@PathVariable Long issueId,
                                                                @RequestBody IssueTopicUpdateRequest request) {
        return ResponseEntity.ok(issueUpdateService.changeIssueTopic(issueId, request));
    }

    @PutMapping("/{issueId}/description")
    public ResponseEntity<IssueDetailResponse> issueDescriptionModify(@PathVariable Long issueId,
                                                                      @RequestBody IssueDescriptionUpdateRequest request) {
        return ResponseEntity.ok(issueUpdateService.changeIssueDescription(issueId, request));
    }

    @PutMapping("/{issueId}/manager")
    public ResponseEntity<IssueDetailResponse> issueManagerModify(@PathVariable Long issueId,
                                                                  @RequestBody IssueManagerUpdateRequest request) {
        return ResponseEntity.ok(issueUpdateService.changeIssueManager(issueId, request));
    }

    @PutMapping("/{issueId}/priority")
    public ResponseEntity<IssueDetailResponse> issuePriorityModify(@PathVariable Long issueId,
                                                                   @RequestBody IssuePriorityUpdateRequest request) {
        return ResponseEntity.ok(issueUpdateService.changeIssuePriority(issueId, request));
    }

    @PutMapping("/{issueId}/category")
    public ResponseEntity<IssueDetailResponse> issueCategoryModify(@PathVariable Long issueId,
                                                                   @RequestBody IssueCategoryUpdateRequest request) {
        return ResponseEntity.ok(issueUpdateService.changeIssueCategory(issueId, request));
    }

    @PutMapping("/{issueId}/progress")
    public ResponseEntity<IssueDetailResponse> issueProgressModify(@PathVariable Long issueId,
                                                                   @RequestBody IssueProgressUpdateRequest request) {
        return ResponseEntity.ok(issueUpdateService.changeIssueProgress(issueId, request));
    }
}
