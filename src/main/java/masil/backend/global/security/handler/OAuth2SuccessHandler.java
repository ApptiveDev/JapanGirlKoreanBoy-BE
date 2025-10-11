package masil.backend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.dto.response.OAuth2SignInResponse;
import masil.backend.modules.member.dto.response.OAuth2UserInfo;
import masil.backend.modules.member.service.OAuth2Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.Builder;
import java.io.IOException;

/**
 * OAuth2 로그인 성공 시 처리하는 핸들러
 * 
 * 구글 OAuth2 로그인이 성공하면 사용자 정보를 추출하고,
 * 기존 회원인지 신규 회원인지 확인하여 적절한 처리를 수행합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2Service oAuth2Service;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        log.info("OAuth2 로그인 성공: {}", authentication.getName());
        
        // OAuth2User 객체에서 사용자 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // 구글에서 제공하는 사용자 정보를 애플리케이션 내부 형식으로 변환
        OAuth2UserInfo userInfo = new OAuth2UserInfo(
            (String) oAuth2User.getAttributes().get("name"),
            (String) oAuth2User.getAttributes().get("email"),
            (String) oAuth2User.getAttributes().get("sub"),
            (String) oAuth2User.getAttributes().get("picture")
    );


        log.info("OAuth2 사용자 정보: name={}, email={}", userInfo.name(), userInfo.email());

        // OAuth2 로그인 처리 (기존 회원 확인, 신규 가입 등)
        OAuth2SignInResponse signInResponse = oAuth2Service.processOAuth2SignIn(userInfo);

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        // JSON 응답 반환
        response.getWriter().write(objectMapper.writeValueAsString(signInResponse));
        
        log.info("OAuth2 로그인 처리 완료: isNewMember={}", signInResponse.isNewMember());
    }

}