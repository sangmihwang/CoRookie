package com.fourttttty.corookie.project.presentation;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.project.application.service.ProjectService;
import com.fourttttty.corookie.project.dto.request.ProjectCreateRequest;
import com.fourttttty.corookie.project.dto.request.ProjectUpdateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectDetailResponse;
import com.fourttttty.corookie.project.dto.response.ProjectListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectListResponse>> projectList(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(projectService.findByParticipantId(loginUser.getMemberId()));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> projectDetail(@PathVariable Long projectId,
                                                               @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(projectService.findById(projectId, loginUser.getMemberId()));
    }

    @PostMapping
    public ResponseEntity<ProjectDetailResponse> projectCreate(@RequestBody @Validated ProjectCreateRequest request,
                                                               @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(projectService.create(request, loginUser.getMemberId()));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> projectModify(@PathVariable Long projectId,
                                                               @RequestBody @Validated ProjectUpdateRequest request,
                                                               @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(projectService.modify(request, projectId, loginUser.getMemberId()));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Object> projectDelete(@PathVariable Long projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{projectId}/invite/enable")
    public ResponseEntity<ProjectDetailResponse> projectInvitationEnable(@PathVariable Long projectId,
                                                                         @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(projectService.enableInvitationStatus(projectId, loginUser.getMemberId()));
    }

    @PutMapping("/{projectId}/invite/disable")
    public ResponseEntity<ProjectDetailResponse> projectInvitationDisable(@PathVariable Long projectId,
                                                                          @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(projectService.disableInvitationStatus(projectId, loginUser.getMemberId()));
    }

    @GetMapping("/invite/{invitationLink}")
    public ResponseEntity<ProjectDetailResponse> projectInvitation(@PathVariable String invitationLink,
                                                                   @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(projectService.findByInvitationLink(invitationLink, loginUser.getMemberId()));
    }
}
