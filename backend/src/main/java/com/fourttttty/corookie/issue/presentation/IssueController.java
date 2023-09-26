package com.fourttttty.corookie.issue.presentation;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.issue.application.service.IssueFilteringService;
import com.fourttttty.corookie.issue.application.service.IssueService;
import com.fourttttty.corookie.issue.dto.request.*;
import com.fourttttty.corookie.issue.dto.response.IssueDetailResponse;
import com.fourttttty.corookie.issue.dto.response.IssueListResponse;
import com.fourttttty.corookie.issue.util.IssueFilterType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/issues")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;
    private final IssueFilteringService issueFilteringService;

    @GetMapping
    public ResponseEntity<List<IssueListResponse>> issueList(@PathVariable Long projectId) {
        return ResponseEntity.ok(issueService.findByProjectId(projectId));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueDetailResponse> issueDetail(@PathVariable Long issueId) {
        return ResponseEntity.ok(issueService.findById(issueId));
    }

    @PostMapping
    public ResponseEntity<IssueDetailResponse> issueCreate(@PathVariable Long projectId,
                                                           @RequestBody
                                                           @Validated
                                                           IssueCreateRequest issueCreateRequest,
                                                           @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(issueService.create(issueCreateRequest, projectId, loginUser.getMemberId()));
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<Object> issueDelete(@PathVariable Long issueId) {
        issueService.deleteById(issueId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<IssueListResponse>> issueListByFiltering(@PathVariable Long projectId,
                                                                        @RequestParam IssueFilterType type,
                                                                        @RequestParam String condition) {
        return ResponseEntity.ok(issueFilteringService.findByFiltering(projectId, type, condition));
    }
}
