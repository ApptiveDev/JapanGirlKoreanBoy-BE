package masil.backend.modules.member.enums;

import lombok.Getter;

@Getter
public enum Asset {
    UNDER_100M("자산 1억 미만"),
    BETWEEN_100M_300M("자산 1억-3억 사이"),
    BETWEEN_300M_500M("자산 3억-5억 사이"),
    BETWEEN_500M_1B("자산 5억-10억 사이"),
    OVER_1B("자산 10억 초과");

    private final String displayName;

    Asset(String displayName) {
        this.displayName = displayName;
    }
}
