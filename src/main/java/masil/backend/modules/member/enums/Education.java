package masil.backend.modules.member.enums;

import lombok.Getter;

@Getter
public enum Education {
    HIGH_SCHOOL("고등학교 졸업"),
    ASSOCIATE_DEGREE("전문학사(2년제 대학)"),
    BACHELOR_DEGREE("학사(4년제 대학)"),
    MASTER_DEGREE("석사"),
    DOCTORATE_DEGREE("박사");

    private final String displayName;

    Education(String displayName) {
        this.displayName = displayName;
    }
}
