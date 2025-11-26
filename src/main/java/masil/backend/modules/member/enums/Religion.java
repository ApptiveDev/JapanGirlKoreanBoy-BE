package masil.backend.modules.member.enums;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public enum Religion {
    NONE(1, "무교"),
    BUDDHISM(2, "불교"),
    CHRISTIANITY(4, "기독교"),
    CATHOLICISM(8, "천주교"),
    TAOISM(16, "도교");

    private final int bitmask;
    private final String displayName;

    Religion(int bitmask, String displayName) {
        this.bitmask = bitmask;
        this.displayName = displayName;
    }

    // 비트마스크에서 종교 리스트로 변환
    public static List<Religion> fromBitmask(Integer bitmask) {
        if (bitmask == null || bitmask == 0) {
            return Collections.emptyList();
        }

        List<Religion> religions = new ArrayList<>();
        for (Religion religion : Religion.values()) {
            if ((bitmask & religion.getBitmask()) != 0) {
                religions.add(religion);
            }
        }
        return religions;
    }

    // 종교 리스트를 비트마스크로 변환
    public static Integer toBitmask(List<Religion> religions) {
        if (religions == null || religions.isEmpty()) {
            return 0;
        }

        int bitmask = 0;
        for (Religion religion : religions) {
            bitmask |= religion.getBitmask();
        }
        return bitmask;
    }
}
