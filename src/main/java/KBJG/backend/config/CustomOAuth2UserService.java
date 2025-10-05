package KBJG.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import KBJG.backend.entity.User;
import KBJG.backend.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    private final UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            log.error("Error processing OAuth2 user", ex);
            throw new OAuth2AuthenticationException("OAuth2 user processing failed");
        }
    }
    
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
        String providerId = (String) attributes.get("sub");
        
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
        
        log.info("Processing OAuth2 user: {}", email);
        
        // 사용자 존재 확인
        User user = userRepository.findByEmailAndProvider(email, User.AuthProvider.GOOGLE)
                .orElseGet(() -> {
                    log.info("Creating new OAuth2 user: {}", email);
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .profileImageUrl(picture)
                            .provider(User.AuthProvider.GOOGLE)
                            .providerId(providerId)
                            .emailVerified(true)
                            .role(User.Role.USER)
                            .build();
                    return userRepository.save(newUser);
                });
        
        return new CustomOAuth2User(user, attributes);
    }
}
