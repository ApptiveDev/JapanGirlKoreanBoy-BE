package masil.backend.modules.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * 커스텀 OAuth2 사용자 서비스
 * 
 * OAuth2 제공자로부터 사용자 정보를 로드하는 역할을 담당
 * 현재는 기본 구현을 그대로 사용하지만, 향후 사용자 정보를 데이터베이스에 저장하거나
 * 추가적인 처리가 필요한 경우 이 클래스에서 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            log.info("OAuth2 사용자 정보 로드 시작: {}", userRequest.getClientRegistration().getRegistrationId());
            log.info("Access token: {}", userRequest.getAccessToken().getTokenValue());
            
            // 부모 클래스의 기본 구현을 사용하여 사용자 정보 로드
            OAuth2User oAuth2User = super.loadUser(userRequest);
            
            log.info("OAuth2 사용자 정보 로드 완료: {}", oAuth2User.getName());
            log.info("OAuth2 사용자 attributes: {}", oAuth2User.getAttributes());
            
            // 향후 사용자 정보를 데이터베이스에 저장하거나 추가 처리가 필요한 경우
            // 여기서 구현할 수 있습니다.
            
            return oAuth2User;
        } catch (Exception e) {
            log.error("OAuth2 사용자 정보 로드 중 오류 발생: {}", e.getMessage(), e);
            throw new OAuth2AuthenticationException("OAuth2 사용자 정보 로드 실패");
        }
    }
}