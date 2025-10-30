package masil.backend.modules.member.service;

import lombok.RequiredArgsConstructor;
import masil.backend.global.security.provider.JwtProvider;
import masil.backend.modules.member.dto.OAuth2TempUserInfo;
import masil.backend.modules.member.dto.request.CompleteOAuth2ProfileRequest;
import masil.backend.modules.member.dto.response.OAuth2SignInResponse;
import masil.backend.modules.member.dto.response.OAuth2UserInfo;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.密室Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2Service {
    
    private final MemberLowService memberLowService;
    private final JwtProvider jwtProvider;

    /**
     * OAuth2 로그인 처리
     * 기존 회원이면 로그인, 신규 회원이면 프로필 정보 입력 필요 플래그 반환
     */
    public OAuth2SignInResponse processOAuth2SignIn(OAuth2UserInfo userInfo) {
        // 기존 회원인지 확인
        Member existingMember = memberLowService.findByEmailAndProvider(
            userInfo.email(),
            Provider.GOOGLE
        );
        
        if (existingMember != null) {
            // 기존 회원 로그인 - 즉시 토큰 발급
            return createSignInResponse(existingMember, false);
        }
        
        // 신규 회원인 경우 - 프로필 정보 입력 필요
        // 회원은 아직 생성하지 않음
        return OAuth2SignInResponse.needsProfileCompletion();
    }

    /**
     * OAuth2 프로필 완성 처리
     * 임시 저장된 OAuth2 정보와 입력받은 프로필 정보로 회원 생성
     */
    public OAuth2SignInResponse completeOAuth2Profile(
            OAuth2TempUserInfo tempUserInfo,
            CompleteOAuth2ProfileRequest profileRequest
    ) {
        // OAuth2 정보 + 프로필 정보로 회원 생성
        Member newMember = memberLowService.saveOAuth2MemberWithProfile(
                tempUserInfo,
                profileRequest
        );
        
        // 회원 생성 후 JWT 토큰 발급 및 로그인 처리
        return createSignInResponse(newMember, true);
    }

    private OAuth2SignInResponse createSignInResponse(Member member, boolean isNewMember) {
        String accessToken = jwtProvider.createToken(member.getId().toString(), member.getName());
        String refreshToken = "";

        return new OAuth2SignInResponse(
                member,
                accessToken,
                refreshToken,
                isNewMember
        );
    }
}