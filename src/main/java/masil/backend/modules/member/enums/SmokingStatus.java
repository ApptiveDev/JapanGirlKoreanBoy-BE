package masil.backend.modules.member.enums;

import lombok.Getter;

/**
 * 흡연 상태 Enum
 */
@Getter
public enum SmokingStatus {
    SMOKER("흡연"),
    NON_SMOKER("비흡연");

    private final String displayName;

    SmokingStatus(String displayName) {
        this.displayName = displayName;
    }
}