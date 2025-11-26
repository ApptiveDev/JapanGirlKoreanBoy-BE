package masil.backend.modules.member.enums;

import lombok.Getter;

@Getter
public enum Gender {
    KOREAN_MALE("한국인 남성"),
    JAPANESE_FEMALE("일본인 여성");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }
}
