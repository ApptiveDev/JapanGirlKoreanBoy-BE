package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;

/**
 * OAuth2 로그인 성공 응답 (record 버전)
 * - JWT 토큰, 사용자 정보, 신규 회원 여부 포함
 */
public record OAuth2SignInResponse(
        Long memberId,
        String name,
        String email,
        String accessToken,
        String refreshToken,
        boolean isNewMember
) {
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
                isNewMember
        );
    }
}
