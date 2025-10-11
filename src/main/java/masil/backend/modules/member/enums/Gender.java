package masil.backend.modules.member.enums;

import lombok.Getter;

/**
 * 회원 성별 Enum
 * 한국인 남성, 일본인 여성만 선택 가능
 */
@Getter
public enum Gender {
    KOREAN_MALE("한국인 남성"),
    JAPANESE_FEMALE("일본인 여성");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }
}