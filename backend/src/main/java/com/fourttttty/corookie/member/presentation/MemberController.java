package com.fourttttty.corookie.member.presentation;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.member.application.service.MemberService;
import com.fourttttty.corookie.member.dto.request.MemberNameUpdateRequest;
import com.fourttttty.corookie.member.dto.request.MemberProfileUpdateRequest;
import com.fourttttty.corookie.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> memberDetails(@PathVariable Long memberId) {
        return ResponseEntity.ok(MemberResponse.from(memberService.findEntityById(memberId)));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> myDetails(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(MemberResponse.from(loginUser.getMember()));
    }

    @PutMapping("/{memberId}/profile")
    public ResponseEntity<MemberResponse> memberProfileModify(@PathVariable Long memberId,
                                                              @AuthenticationPrincipal LoginUser loginUser,
                                                              @RequestBody @Validated MemberProfileUpdateRequest request) {
        if (isInvalidAuth(memberId, loginUser)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(memberService.updateProfileUrl(request, memberId));
    }

    @PutMapping("/{memberId}/name")
    public ResponseEntity<MemberResponse> memberNameModify(@PathVariable Long memberId,
                                                           @AuthenticationPrincipal LoginUser loginUser,
                                                           @RequestBody @Validated MemberNameUpdateRequest request) {
        if (isInvalidAuth(memberId, loginUser)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(memberService.updateName(request, memberId));
    }

    private static boolean isInvalidAuth(Long memberId, LoginUser loginUser) {
        return !memberId.equals(loginUser.getMemberId());
    }
}
