package masil.backend.global.security.repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.util.WebUtils;

import java.util.Base64;

@Slf4j
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        // 고정된 쿠키 이름 사용 (state를 이름에 붙이지 않음)
        String state = request.getParameter("state");
        log.info("OAuth2 쿠키 로드 시도 - state: {}, 요청 URI: {}, 모든 쿠키: {}", 
                state, request.getRequestURI(), request.getCookies() != null ? 
                        java.util.Arrays.toString(java.util.Arrays.stream(request.getCookies())
                                .map(c -> c.getName()).toArray(String[]::new)) : "null");
        
        var cookie = WebUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME);
        
        if (cookie != null) {
            try {
                OAuth2AuthorizationRequest authRequest = deserialize(cookie.getValue());
                
                // state 검증 (쿠키 이름이 아닌 쿠키 값의 state와 비교)
                if (state != null && !state.equals(authRequest.getState())) {
                    log.warn("OAuth2 state 불일치 - 요청 state: {}, 쿠키 state: {}", state, authRequest.getState());
                    return null;
                }
                
                log.info("OAuth2 인증 요청 쿠키 로드 성공 - clientId: {}, state: {}", 
                        authRequest.getClientId(), authRequest.getState());
                return authRequest;
            } catch (Exception e) {
                log.error("OAuth2 쿠키 역직렬화 실패", e);
                return null;
            }
        }
        
        log.warn("OAuth2 인증 요청 쿠키를 찾을 수 없습니다. 요청 URI: {}, state: {}", request.getRequestURI(), state);
        return null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            log.info("OAuth2 인증 요청이 null이므로 쿠키 제거");
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        try {
            var serialized = serialize(authorizationRequest);
            int cookieSize = serialized.length();
            String state = authorizationRequest.getState();
            
            log.info("OAuth2 인증 요청 쿠키 저장 시도 - 직렬화 크기: {} bytes, clientId: {}, state: {}", 
                    cookieSize, authorizationRequest.getClientId(), state);
            
            // 쿠키 크기 제한 체크 (일반적으로 4KB 제한)
            if (cookieSize > 4096) {
                log.error("OAuth2 쿠키 크기가 너무 큽니다: {} bytes (최대 4096 bytes). 쿠키 저장 실패 가능성 높음.", cookieSize);
            }
            
            // 기본 쿠키 저장
            var cookie = new jakarta.servlet.http.Cookie(OAUTH2_AUTH_REQUEST_COOKIE_NAME, serialized);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(COOKIE_EXPIRE_SECONDS);
            cookie.setSecure(false); // 로컬 개발 환경을 위해 false로 설정
            
            log.info("OAuth2 쿠키 설정 - Name: {}, Path: {}, HttpOnly: {}, Secure: {}, MaxAge: {}", 
                    cookie.getName(), cookie.getPath(), cookie.isHttpOnly(), cookie.getSecure(), cookie.getMaxAge());
            
            response.addCookie(cookie);
            log.info("OAuth2 인증 요청 쿠키 저장 완료 - clientId: {}, state: {}", authorizationRequest.getClientId(), state);
        } catch (Exception e) {
            log.error("OAuth2 인증 요청 쿠키 저장 실패", e);
            e.printStackTrace();
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("OAuth2 인증 요청 제거 시도 - 요청 URI: {}", request.getRequestURI());
        var authorizationRequest = loadAuthorizationRequest(request);
        removeAuthorizationRequestCookies(request, response);
        log.info("OAuth2 인증 요청 제거 완료");
        return authorizationRequest;
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        // 고정된 쿠키 이름으로만 제거 (state를 이름에 붙이지 않음)
        var cookie = new jakarta.servlet.http.Cookie(OAUTH2_AUTH_REQUEST_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);
        
        log.info("OAuth2 쿠키 제거 완료");
    }

    private String serialize(OAuth2AuthorizationRequest authorizationRequest) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(authorizationRequest));
    }

    private OAuth2AuthorizationRequest deserialize(String cookie) {
        return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie));
    }
}
