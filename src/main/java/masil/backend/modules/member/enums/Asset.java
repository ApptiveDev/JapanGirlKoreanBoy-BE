package masil.backend.modules.member.enums;

import lombok.Getter;

/**
 * 재산 Enum (인증 필요)
 */
@Getter
public enum Asset {
    UNDER_100M("1억 미만"),
    BETWEEN_100M_300M("1억 - 3억"),
    BETWEEN_300M_500M("3억 - 5억"),
    BETWEEN_500M_1B("5억 - 10억"),
    OVER_1B("10억 이상");

    private final String displayName;

    Asset(String displayName) {
        this.displayName = displayName;
    }
}