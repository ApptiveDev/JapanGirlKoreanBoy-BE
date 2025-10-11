package masil.backend.modules.member.enums;

import lombok.Getter;

/**
 * 종교 Enum
 */
@Getter
public enum Religion {
    NONE("무교"),
    CHRISTIANITY("기독교"),
    BUDDHISM("불교"),
    CATHOLICISM("천주교"),
    ISLAM("이슬람교"),
    HINDUISM("힌두교"),
    OTHER("기타");

    private final String displayName;

    Religion(String displayName) {
        this.displayName = displayName;
    }
}