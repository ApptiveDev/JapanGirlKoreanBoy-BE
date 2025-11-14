package masil.backend.modules.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PreferenceCategory {
    JOB("직업"),
    EDUCATION("학벌"),
    HEIGHT("키"),
    APPEARANCE("외모"),
    PARENT_ASSET("부모님 자산"),
    ASSET("자산"),
    RELIGION("종교"),
    PERSONALITY("성격");

    private final String description;
}
