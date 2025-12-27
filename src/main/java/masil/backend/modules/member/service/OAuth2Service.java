package masil.backend.modules.member.service;
import lombok.RequiredArgsConstructor;
import masil.backend.global.security.provider.JwtProvider;
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
            // 기존 회원인 경우
            String accessToken = jwtProvider.createToken(
                    existingMember.getId().toString(), 
                    existingMember.getName()
            );
            
            // 프로필 완성 여부 확인
            boolean needsProfile = !existingMember.isProfileComplete();
            return OAuth2SignInResponse.signedIn(existingMember, accessToken, needsProfile);
        }
        
        // 신규 회원인 경우: 기본 정보만으로 회원 생성
        Member newMember = memberLowService.saveOAuth2Member(userInfo);
        
        // 신규 회원도 즉시 토큰 발급 (프로필 완성 필요 플래그는 true)
        String accessToken = jwtProvider.createToken(
                newMember.getId().toString(), 
                newMember.getName()
        );
        return OAuth2SignInResponse.signedIn(newMember, accessToken, true);
    }

    @Transactional
    public OAuth2SignInResponse completeOAuth2Profile(
            final Long memberId,
            final CompleteOAuth2ProfileRequest request
    ) {
        // 기존 회원 조회
        Member member = memberLowService.getValidateExistMemberById(memberId);
        
        // 프로필 정보 업데이트
        memberLowService.updateOAuth2MemberProfile(member, request);
        
        // 프로필 완성 후 토큰 발급
        String accessToken = jwtProvider.createToken(member.getId().toString(), member.getName());
        return OAuth2SignInResponse.signedIn(member, accessToken, false);
    }

}