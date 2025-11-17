package masil.backend.modules.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParentAssetLevel {
    OVER_100M("상속받을 자산이 1억 이상 있어야 해요"),
    RETIREMENT_ONLY("노후관리만 되어 있으면 돼요"),
    NO_CONCERN("상관없어요");

    private final String description;
}
