package masil.backend.modules.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EducationLevel {
    TOP_TIER("상위권 명문대"),
    PRESTIGIOUS("명문대"),
    MID_TIER("중상위권 국공립·사립대"),
    PRACTICAL("실무형·예술·전문대");

    private final String description;
}
