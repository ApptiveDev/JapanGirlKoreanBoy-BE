package masil.backend.modules.member.dto;

import masil.backend.modules.member.dto.response.OAuth2UserInfo;

/**
 * OAuth2 로그인 후 프로필 완성 전까지 세션에 임시 저장할 사용자 정보
 */
public record OAuth2TempUserInfo(
        String name,
        String email,
        String providerId,
        String profileImageUrl
) {
    public static OAuth2TempUserInfo from(OAuth2UserInfo userInfo) {
        return new OAuth2TempUserInfo(
                userInfo.name(),
                userInfo.email(),
                userInfo.providerId(),
                userInfo.profileImageUrl()
        );
    }
}