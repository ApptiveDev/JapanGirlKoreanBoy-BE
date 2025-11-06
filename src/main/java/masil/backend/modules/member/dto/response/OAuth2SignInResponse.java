package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;

/**
 * OAuth2 로그인 성공 응답
 */
public record OAuth2SignInResponse(
        Long memberId,
        String name,
        String email,
        String accessToken,
        String refreshToken,
        boolean needsProfileCompletion  // 프로필 완성 필요 여부
) {
    // 로그인 완료(기존 회원 등): 토큰 포함, 프로필 입력 불필요
    public static OAuth2SignInResponse signedIn(final Member member, final String accessToken) {
        return new OAuth2SignInResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                accessToken,
                "",                 // refreshToken 미사용
                false               // 프로필 입력 불필요
        );
    }

    // 신규 + 프로필 미완성: 회원 미생성, 토큰 없음
    public static OAuth2SignInResponse needsProfile() {
        return new OAuth2SignInResponse(
                null, userInfo.name(), userInfo.email(),
                null, "",
                true                // 프로필 입력 필요
        );
    }
}