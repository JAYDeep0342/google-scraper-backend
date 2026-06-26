package BDM.scraper.security;

import BDM.scraper.dto.LoginResponseDto;
import BDM.scraper.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    private static final String FRONTEND_CALLBACK = "http://localhost:5173/oauth2/callback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String registrationId = token.getAuthorizedClientRegistrationId();

        ResponseEntity<LoginResponseDto> loginResponse =
                authService.handleOAuth2LoginRequest(oAuth2User, registrationId);

        LoginResponseDto body = loginResponse.getBody();

        String roles = body.getRoles().stream()
                .map(r -> URLEncoder.encode(r.name(), StandardCharsets.UTF_8))
                .collect(Collectors.joining(","));

        String email = oAuth2User.getAttribute("email");
        String encodedEmail = email != null
                ? URLEncoder.encode(email, StandardCharsets.UTF_8)
                : "";

        String redirectUrl = FRONTEND_CALLBACK
                + "?jwt="    + URLEncoder.encode(body.getJwt(), StandardCharsets.UTF_8)
                + "&userid=" + body.getUserid()
                + "&roles="  + roles
                + "&email="  + encodedEmail;

        response.sendRedirect(redirectUrl);
    }
}
