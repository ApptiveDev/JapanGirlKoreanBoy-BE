package masil.backend.modules.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppearanceStyle {
    CELEBRITY("연예인 느낌의 비주얼이에요"),
    NATURAL("자연스럽고 호감 가는 인상이에요"),
    UNIQUE("개성이 뚜렷하고 자기 스타일이 있는 사람이에요");

    private final String description;
}
