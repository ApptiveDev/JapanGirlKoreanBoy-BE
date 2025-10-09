package masil.backend.modules.member.dto.response;

/**
 * OAuth2 제공자로부터 받은 사용자 정보를 담는 DTO (record)
 */
public record OAuth2UserInfo(
        String name,
        String email,
        String providerId,
        String profileImageUrl
) { }
