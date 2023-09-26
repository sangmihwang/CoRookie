package com.fourttttty.corookie.config.security.oauth2;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.config.security.jwt.JwtSetupService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtSetupService jwtSetupService;

    @Value("${client.url}")
    private String clientUrl;

    @Value("${client.endpoint}")
    private String redirectEndPoint;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        getRedirectStrategy().sendRedirect(request, response, clientUrl +
                "/" + redirectEndPoint +
                "?token=" + jwtSetupService.createAuthToken((LoginUser) authentication.getPrincipal()));
    }
}
