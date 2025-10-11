package masil.backend.modules.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 관련 API를 처리하는 컨트롤러
 * 
 * OAuth2 로그인 관련 엔드포인트를 제공합니다.
 */
@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    /**
     * 구글 OAuth2 로그인 페이지로 리다이렉트
     * 클라이언트가 이 엔드포인트에 접근하면 구글 OAuth2 로그인 페이지로 리다이렉트됩니다.
    */
    @GetMapping("/login/google")
    public ResponseEntity<String> googleLogin() {
        log.info("구글 OAuth2 로그인 요청");
        return ResponseEntity.ok("Google OAuth2 로그인 페이지로 리다이렉트됩니다. /oauth2/authorization/google로 접근하세요.");
    }
}