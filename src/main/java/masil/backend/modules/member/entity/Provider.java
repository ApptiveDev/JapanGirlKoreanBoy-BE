package masil.backend.modules.member.entity;

import lombok.Getter;

/**
 * 회원 가입 제공자 Enum
 */
@Getter
public enum Provider {
    /** 일반 회원가입 */
    LOCAL("일반 회원가입"),
    /** 구글 OAuth2 로그인 */
    GOOGLE("구글 로그인");

    private final String displayName;

    Provider(String displayName) {
        this.displayName = displayName;
    }
}