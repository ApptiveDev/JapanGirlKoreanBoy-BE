package masil.backend.modules.member.service;
import lombok.RequiredArgsConstructor;
import masil.backend.global.security.provider.JwtProvider;
import masil.backend.modules.member.dto.OAuth2TempUserInfo;
import masil.backend.modules.member.dto.request.CompleteOAuth2ProfileRequest;
import masil.backend.modules.member.dto.response.OAuth2SignInResponse;
import masil.backend.modules.member.dto.response.OAuth2UserInfo;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2Service {
    
    private final MemberLowService memberLowService;
    private final JwtProvider jwtProvider;

    public OAuth2SignInResponse processOAuth2SignIn(OAuth2UserInfo userInfo) {
        Member existingMember = memberLowService.findByEmailAndProvider(
                userInfo.email(),
                Provider.GOOGLE
        );
        if (existingMember != null) {
            String accessToken = jwtProvider.createToken(
                    existingMember.getId().toString(), 
                    existingMember.getName()
            );
            return OAuth2SignInResponse.signedIn(existingMember, accessToken);
        }
        // 신규 + 필수정보 미입력: 회원 미생성, 프로필 입력 필요 플래그만 반환
        return OAuth2SignInResponse.needsProfile(userInfo);
    }

    @Transactional
    public OAuth2SignInResponse completeOAuth2Profile(
            final OAuth2TempUserInfo tempUserInfo,
            final CompleteOAuth2ProfileRequest request
    ) {
        // 프로필 포함 신규 회원 생성
        Member newMember = memberLowService.saveOAuth2MemberWithProfile(tempUserInfo, request);

        // 토큰 발급 후 로그인 완료 응답(needsProfileCompletion=false)
        String accessToken = jwtProvider.createToken(newMember.getId().toString(), newMember.getName());
        return OAuth2SignInResponse.signedIn(newMember, accessToken);
    }

}