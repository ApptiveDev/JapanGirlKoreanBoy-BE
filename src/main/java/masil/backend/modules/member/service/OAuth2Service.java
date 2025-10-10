package masil.backend.modules.member.service;

import lombok.RequiredArgsConstructor;
import masil.backend.global.security.provider.JwtProvider;
import masil.backend.modules.member.dto.response.OAuth2SignInResponse;
import masil.backend.modules.member.dto.response.OAuth2UserInfo;
import masil.backend.modules.member.entity.*;
import masil.backend.modules.member.enums.Provider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2 로그인 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 
 * 구글 OAuth2 로그인 처리, 신규 회원 가입, 기존 회원 로그인 등의 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2Service {
    
    private final MemberLowService memberLowService;
    private final JwtProvider jwtProvider;

    /**
     * OAuth2 로그인 처리
     * 
     * 기존 회원인지 확인하고, 기존 회원이면 로그인, 신규 회원이면 가입 후 로그인을 처리합니다.
     * 
     * @param userInfo OAuth2 제공자로부터 받은 사용자 정보
     * @return OAuth2SignInResponse 로그인 응답 정보
     */
    public OAuth2SignInResponse processOAuth2SignIn(OAuth2UserInfo userInfo) {
        // 기존 회원인지 확인 (이메일과 제공자로 조회)
        Member existingMember = memberLowService.findByEmailAndProvider(
            userInfo.email(),
                Provider.GOOGLE
        );
        
        if (existingMember != null) {
            // 기존 회원 로그인
            return createSignInResponse(existingMember, false);
        }
        // 신규 회원 가입
        Member newMember = memberLowService.saveOAuth2Member(userInfo);
        return createSignInResponse(newMember, true);

    }

    /**
     * 로그인 응답 객체 생성
     * 
     * JWT 토큰을 생성하고 사용자 정보와 함께 응답 객체를 만듭니다.
     * 
     * @param member 회원 정보
     * @param isNewMember 신규 회원 여부
     * @return OAuth2SignInResponse 로그인 응답 정보
     */
    private OAuth2SignInResponse createSignInResponse(Member member, boolean isNewMember) {
        // JWT 토큰 생성
        String accessToken = jwtProvider.createToken(member.getId().toString(), member.getName());
        
        // 리프레시 토큰은 향후 구현 예정이므로 현재는 빈 문자열
        String refreshToken = "";

        return new OAuth2SignInResponse(
                member,
                accessToken,
                refreshToken,
                isNewMember
        );
    }
}