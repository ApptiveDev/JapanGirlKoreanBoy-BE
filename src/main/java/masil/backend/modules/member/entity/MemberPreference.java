package masil.backend.modules.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import masil.backend.global.base.BaseEntity;
import masil.backend.modules.member.enums.AppearanceStyle;
import masil.backend.modules.member.enums.EducationLevel;
import masil.backend.modules.member.enums.ParentAssetLevel;
import masil.backend.modules.member.enums.PreferenceCategory;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPreference extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    // 선호 키 범위
    @Column
    private Integer preferredHeightMin;

    @Column
    private Integer preferredHeightMax;

    // 기피 종교 (비트마스킹)
    // NONE(1), BUDDHISM(2), CHRISTIANITY(4), CATHOLICISM(8), SHINTO(16), OTHER(32)
    @Column
    private Integer avoidReligionsBitmask;

    // 학벌 선호
    @Enumerated(EnumType.STRING)
    @Column
    private EducationLevel preferredEducationLevel;

    // 외모 스타일 선호
    @Enumerated(EnumType.STRING)
    @Column
    private AppearanceStyle preferredAppearanceStyle;

    // 부모님 자산 요구사항
    @Enumerated(EnumType.STRING)
    @Column
    private ParentAssetLevel parentAssetRequirement;

    // 선호 자산 범위 (단위: 원)
    @Column
    private Long preferredAssetMin;

    @Column
    private Long preferredAssetMax;

    // 선호 직업 (JSON 배열로 저장, 최대 3개)
    @Column(columnDefinition = "JSON")
    private String preferredJobs; // ["DOCTOR", "TEACHER", "ENGINEER"]

    // 비선호 직업 (JSON 배열로 저장, 최대 3개)
    @Column(columnDefinition = "JSON")
    private String avoidedJobs; // ["FREELANCER", "UNEMPLOYED", "OTHER"]

    // MBTI 선호 (각 축별로 NOT NULL, 상관없으면 "X"로 표시)
    @Column(length = 1, nullable = false)
    private String mbtiE; // "E" or "I" or "X"(상관없음)

    @Column(length = 1, nullable = false)
    private String mbtiN; // "N" or "S" or "X"(상관없음)

    @Column(length = 1, nullable = false)
    private String mbtiT; // "T" or "F" or "X"(상관없음)

    @Column(length = 1, nullable = false)
    private String mbtiJ; // "J" or "P" or "X"(상관없음)

    @Enumerated(EnumType.STRING)
    @Column
    private PreferenceCategory priority1;

    @Enumerated(EnumType.STRING)
    @Column
    private PreferenceCategory priority2;

    @Enumerated(EnumType.STRING)
    @Column
    private PreferenceCategory priority3;

    public MemberPreference(
            Member member,
            Integer preferredHeightMin,
            Integer preferredHeightMax,
            Integer avoidReligionsBitmask,
            EducationLevel preferredEducationLevel,
            AppearanceStyle preferredAppearanceStyle,
            ParentAssetLevel parentAssetRequirement,
            Long preferredAssetMin,
            Long preferredAssetMax,
            String preferredJobs,
            String avoidedJobs,
            String mbtiE,
            String mbtiN,
            String mbtiT,
            String mbtiJ,
            PreferenceCategory priority1,
            PreferenceCategory priority2,
            PreferenceCategory priority3
    ) {
        this.member = member;
        this.preferredHeightMin = preferredHeightMin;
        this.preferredHeightMax = preferredHeightMax;
        this.avoidReligionsBitmask = avoidReligionsBitmask;
        this.preferredEducationLevel = preferredEducationLevel;
        this.preferredAppearanceStyle = preferredAppearanceStyle;
        this.parentAssetRequirement = parentAssetRequirement;
        this.preferredAssetMin = preferredAssetMin;
        this.preferredAssetMax = preferredAssetMax;
        this.preferredJobs = preferredJobs;
        this.avoidedJobs = avoidedJobs;
        this.mbtiE = mbtiE;
        this.mbtiN = mbtiN;
        this.mbtiT = mbtiT;
        this.mbtiJ = mbtiJ;
        this.priority1 = priority1;
        this.priority2 = priority2;
        this.priority3 = priority3;
    }

    public void updatePreference(
            Integer preferredHeightMin,
            Integer preferredHeightMax,
            Integer avoidReligionsBitmask,
            EducationLevel preferredEducationLevel,
            AppearanceStyle preferredAppearanceStyle,
            ParentAssetLevel parentAssetRequirement,
            Long preferredAssetMin,
            Long preferredAssetMax,
            String preferredJobs,
            String avoidedJobs,
            String mbtiE,
            String mbtiN,
            String mbtiT,
            String mbtiJ
    ) {
        this.preferredHeightMin = preferredHeightMin;
        this.preferredHeightMax = preferredHeightMax;
        this.avoidReligionsBitmask = avoidReligionsBitmask;
        this.preferredEducationLevel = preferredEducationLevel;
        this.preferredAppearanceStyle = preferredAppearanceStyle;
        this.parentAssetRequirement = parentAssetRequirement;
        this.preferredAssetMin = preferredAssetMin;
        this.preferredAssetMax = preferredAssetMax;
        this.preferredJobs = preferredJobs;
        this.avoidedJobs = avoidedJobs;
        this.mbtiE = mbtiE;
        this.mbtiN = mbtiN;
        this.mbtiT = mbtiT;
        this.mbtiJ = mbtiJ;
    }
}
