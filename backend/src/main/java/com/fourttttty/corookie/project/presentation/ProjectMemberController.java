package com.fourttttty.corookie.project.presentation;


import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.project.application.service.ProjectMemberService;
import com.fourttttty.corookie.project.dto.request.ProjectMemberCreateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectId}/projectmembers")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<ProjectMemberResponse> projectMemberCreate(@RequestBody @Validated ProjectMemberCreateRequest request) {
        return ResponseEntity.ok(projectMemberService.create(request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Object> projectMemberDelete(@PathVariable Long memberId,
                                                      @PathVariable Long projectId) {
        projectMemberService.delete(memberId, projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> projectMemberList(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectMemberService.findByProjectId(projectId));
    }

    @GetMapping("/me")
    public ResponseEntity<Object> checkMeInProjectMember(@PathVariable Long projectId,
                                                         @AuthenticationPrincipal LoginUser loginUser) {
        if (projectMemberService.existsProjectMember(projectId, loginUser.getMemberId())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
