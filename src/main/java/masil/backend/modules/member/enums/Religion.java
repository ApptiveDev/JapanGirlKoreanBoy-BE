package masil.backend.modules.member.enums;

import lombok.Getter;

@Getter
public enum Religion {
    NONE("무교"),
    BUDDHISM("불교"),
    CHRISTIANITY("기독교"),
    CATHOLICISM("천주교"),
    OTHER("기타");

    private final String displayName;

    Religion(String displayName) {
        this.displayName = displayName;
    }
}
