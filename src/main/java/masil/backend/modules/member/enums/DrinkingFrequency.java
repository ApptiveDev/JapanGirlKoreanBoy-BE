package masil.backend.modules.member.enums;

import lombok.Getter;

/**
 * 음주 빈도 Enum
 */
@Getter
public enum DrinkingFrequency {
    NEVER("전혀 안함"),
    OCCASIONALLY("가끔"),
    FREQUENTLY("자주"),
    DAILY("매일");

    private final String displayName;

    DrinkingFrequency(String displayName) {
        this.displayName = displayName;
    }
}