package BDM.scraper.service;

import BDM.scraper.dto.LoginRequestDto;
import BDM.scraper.dto.LoginResponseDto;
import BDM.scraper.dto.SignupRequestDto;
import BDM.scraper.dto.SignupResponseDto;
import BDM.scraper.entity.User;
import BDM.scraper.entity.type.AuthProviderType;
import BDM.scraper.entity.type.RoleType;
import BDM.scraper.repository.UserRepository;
import BDM.scraper.security.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    public SignupResponseDto signup(SignupRequestDto dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .email(dto.getEmail())
                .roles(Set.of(RoleType.USER))
                .build();

        userRepository.save(user);

        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    public LoginResponseDto login(LoginRequestDto dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(
                token,
                user.getId(),
                user.getRoles()
        );
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(
            OAuth2User oAuth2User,
            String registrationId
    ) {
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (user == null) {

            User emailUser = userRepository.findByUsername(email).orElse(null);

            if (emailUser != null) {
                throw new BadCredentialsException(
                        "This email is already registered with a different provider: " + email
                );
            }

            String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
            user = signUpInternal(username, name, providerType, providerId);

        } else {
            if (email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setUsername(email);
                userRepository.save(user);
            }
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(
                authUtil.generateAccessToken(user),
                user.getId(),
                user.getRoles()
        );

        return ResponseEntity.ok(loginResponseDto);
    }

    private User signUpInternal(
            String username,
            String name,
            AuthProviderType providerType,
            String providerId
    ) {
        User user = User.builder()
                .username(username)
                .password("")
                .name(name != null ? name : username)
                .roles(Set.of(RoleType.USER))
                .providerType(providerType)
                .providerId(providerId)
                .build();

        return userRepository.save(user);
    }
}
