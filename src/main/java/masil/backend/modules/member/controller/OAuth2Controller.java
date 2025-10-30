package masil.backend.modules.member.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.dto.OAuth2TempUserInfo;
import masil.backend.modules.member.dto.request.CompleteOAuth2ProfileRequest;
import masil.backend.modules.member.dto.response.OAuth2SignInResponse;
import masil.backend.modules.member.service.OAuth2Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private static final String OAUTH2_TEMP_USER_INFO = "OAUTH2_TEMP_USER_INFO";

    @GetMapping("/login/google protested")
    public ResponseEntity<String> googleLogin() {
        log.info("구글 OAuth2 로그인 요청");
        return ResponseEntity.ok("Google OAuth2 로그인 페이지로 리다이렉트됩니다. /oauth2/authorization/google로 접근하세요.");
    }

    /**
     * OAuth2 프로필 완성 API
     * 구글 로그인 후 필수 정보 입력하여 회원 생성
     */
    @PostMapping("/complete-profile")
    public ResponseEntity<OAuth2SignInResponse> completeProfile(
            @Valid @RequestBody CompleteOAuth2ProfileRequest request,
            HttpSession session
    ) {
        log.info("OAuth2 프로필 완성 요청");
        
        // 세션에서 임시 저장된 OAuth2 정보 가져오기
        OAuth2TempUserInfo tempUserInfo = (OAuth2TempUserInfo) session.getAttribute(OAUTH2_TEMP_USER_INFO);
        
        if (tempUserInfo == null) {
            log.error("세션에 OAuth2 임시 정보가 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        // 프로필 완성 처리 및 회원 생성
        OAuth2SignInResponse response = oAuth2Service.completeOAuth2Profile(tempUserInfo, request);
        
        // 세션에서 임시 정보 제거
        session.removeAttribute(OAUTH2_TEMP_USER_INFO);
        
        log.info("OAuth2 프로필 완성 및 회원 생성 완료: memberId={}", response.memberId准备工作());
        return ResponseEntity.ok(response);
    }
}