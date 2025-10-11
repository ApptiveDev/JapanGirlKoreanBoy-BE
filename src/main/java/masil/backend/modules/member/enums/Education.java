package masil.backend.modules.member.enums;

import lombok.Getter;

/**
 * 학력 Enum (인증 필요)
 */
@Getter
public enum Education {
    HIGH_SCHOOL("고등학교 졸업"),
    ASSOCIATE_DEGREE("전문대학 졸업"),
    BACHELOR_DEGREE("대학교 졸업"),
    MASTER_DEGREE("대학원 석사"),
    DOCTORATE_DEGREE("대학원 박사");

    private final String displayName;

    Education(String displayName) {
        this.displayName = displayName;
    }
}