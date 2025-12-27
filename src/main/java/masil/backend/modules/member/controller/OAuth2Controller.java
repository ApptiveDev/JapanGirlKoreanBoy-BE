package masil.backend.modules.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.dto.request.CompleteOAuth2ProfileRequest;
import masil.backend.modules.member.dto.response.OAuth2SignInResponse;
import masil.backend.modules.member.service.OAuth2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/google")
    public ResponseEntity<String> googleLogin() {
        log.info("구글 OAuth2 로그인 요청");
        return ResponseEntity.ok("Google OAuth2 로그인 페이지로 리다이렉트됩니다. /oauth2/authorization/google로 접근하세요.");
    }

    /**
     * OAuth2 프로필 완성 API
     * 구글 로그인 후 필수 정보 입력하여 프로필 완성
     */
    @PostMapping("/complete-profile/{memberId}")
    public ResponseEntity<OAuth2SignInResponse> completeProfile(
            @PathVariable Long memberId,
            @Valid @RequestBody CompleteOAuth2ProfileRequest request
    ) {
        log.info("OAuth2 프로필 완성 요청 - memberId: {}", memberId);
        
        // 프로필 완성 처리
        OAuth2SignInResponse response = oAuth2Service.completeOAuth2Profile(memberId, request);
        
        log.info("OAuth2 프로필 완성 완료: memberId={}", response.memberId());
        return ResponseEntity.ok(response);
    }
}