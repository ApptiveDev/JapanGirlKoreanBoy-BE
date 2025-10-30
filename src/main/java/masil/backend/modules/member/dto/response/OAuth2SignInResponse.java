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
        boolean isNewMember,
        boolean needsProfileCompletion  // 프로필 완성 필요 여부
) {
    // 기존 회원인 경우
    public OAuth2SignInResponse(
            final Member member,
            final String accessToken,
            final String refreshToken,
            final boolean isNewMember
    ) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail(),
                accessToken,
                refreshToken,
                isNewMember,
                false  // 기존 회원은 프로필 완성 불필요
        );
    }
    
    // 신규 회원이고 프로필 완성이 필요한 경우
    public static OAuth2SignInResponse needsProfile() {
        return new OAuth2SignInResponse(
                null,
                null,
                null,
                null,  // 토큰 없음
                "",
                true,  // 신규 회원
                true   // 프로필 완성 필요
        );
    }
}