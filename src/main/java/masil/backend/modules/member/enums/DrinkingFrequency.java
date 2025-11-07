package masil.backend.modules.member.enums;

import lombok.Getter;

@Getter
public enum DrinkingFrequency {
    LESS_THAN_ONCE_A_WEEK("주 1회 미만"),
    ONCE_A_WEEK("주 1회"),
    TWICE_A_WEEK("주 2회"),
    MORE_THAN_THREE_TIMES_A_WEEK("주 3회 이상");

    private final String displayName;

    DrinkingFrequency(String displayName) {
        this.displayName = displayName;
    }
}
