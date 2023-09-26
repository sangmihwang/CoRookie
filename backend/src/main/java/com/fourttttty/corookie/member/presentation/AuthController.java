package com.fourttttty.corookie.member.presentation;

import com.fourttttty.corookie.config.security.jwt.JwtSetupService;
import com.fourttttty.corookie.member.dto.request.AuthRequest;
import com.fourttttty.corookie.member.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtSetupService jwtSetupService;

    @PostMapping
    public ResponseEntity<AuthResponse> authToken(@RequestBody @Validated AuthRequest authRequest) {
        return ResponseEntity.ok(AuthResponse.from(jwtSetupService.createJwtTokenByAuthToken(authRequest.authToken())));
    }
}
