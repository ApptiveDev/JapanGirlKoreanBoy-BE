package masil.backend.modules.member.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import masil.backend.modules.member.enums.AppearanceStyle;
import masil.backend.modules.member.enums.Asset;
import masil.backend.modules.member.enums.DrinkingFrequency;
import masil.backend.modules.member.enums.Education;
import masil.backend.modules.member.enums.EducationLevel;
import masil.backend.modules.member.enums.Gender;
import masil.backend.modules.member.enums.JobType;
import masil.backend.modules.member.enums.ParentAssetLevel;
import masil.backend.modules.member.enums.PreferenceCategory;
import masil.backend.modules.member.enums.Religion;

import java.util.List;
import masil.backend.modules.member.enums.SmokingStatus;

public record SaveMemberPreferenceRequest(
        @NotNull(message = "성별을 선택해주세요.")
        Gender gender,

        @NotNull(message = "키를 입력해주세요.")
        Integer height,

        @NotNull(message = "몸무게를 입력해주세요.")
        Integer weight,

        @NotNull(message = "거주 지역을 입력해주세요.")
        String residenceArea,

        @NotNull(message = "흡연 유무를 선택해주세요.")
        SmokingStatus smokingStatus,

        @NotNull(message = "음주 빈도를 선택해주세요.")
        DrinkingFrequency drinkingFrequency,

        @NotNull(message = "종교를 선택해주세요.")
        Religion religion,

        @NotNull(message = "학력을 선택해주세요.")
        Education education,

        @NotNull(message = "자산을 선택해주세요.")
        Asset asset,

        @NotNull(message = "기타 정보를 입력해주세요.")
        @Size(max = 300, message = "기타 정보는 최대 300자까지 입력 가능합니다.")
        String otherInfo,

        @NotNull(message = "프로필 사진을 최소 1장 이상 등록해주세요.")
        @Size(min = 1, message = "프로필 사진을 최소 1장 이상 등록해주세요.")
        List<String> profileImageUrls,

        @NotNull(message = "썸네일 이미지를 지정해주세요.")
        String thumbnailImageUrl,

        @NotNull(message = "선호 키 최소값을 입력해주세요.")
        @Min(value = 130, message = "선호 키 최소값은 130 이상이어야 합니다.")
        @Max(value = 230, message = "선호 키 최소값은 230 이하여야 합니다.")
        Integer preferredHeightMin,

        @NotNull(message = "선호 키 최대값을 입력해주세요.")
        @Min(value = 130, message = "선호 키 최대값은 130 이상이어야 합니다.")
        @Max(value = 230, message = "선호 키 최대값은 230 이하여야 합니다.")
        Integer preferredHeightMax,

        @NotNull(message = "기피 종교를 선택해주세요.")
        List<Religion> avoidReligions,

        @NotNull(message = "선호 학벌을 선택해주세요.")
        EducationLevel preferredEducationLevel,

        @NotNull(message = "선호 외모 스타일을 선택해주세요.")
        AppearanceStyle preferredAppearanceStyle,

        @NotNull(message = "부모님 자산 요구사항을 선택해주세요.")
        ParentAssetLevel parentAssetRequirement,

        @NotNull(message = "선호 자산 최소값을 입력해주세요.")
        @Min(value = 0, message = "선호 자산 최소값은 0 이상이어야 합니다.")
        Long preferredAssetMin,

        @NotNull(message = "선호 자산 최대값을 입력해주세요.")
        @Min(value = 0, message = "선호 자산 최대값은 0 이상이어야 합니다.")
        Long preferredAssetMax,

        @NotNull(message = "선호 직업을 선택해주세요.")
        @Size(max = 3, message = "선호 직업은 최대 3개까지 선택 가능합니다.")
        List<JobType> preferredJobs,

        @NotNull(message = "비선호 직업을 선택해주세요.")
        @Size(max = 3, message = "비선호 직업은 최대 3개까지 선택 가능합니다.")
        List<JobType> avoidedJobs,

        @NotNull(message = "MBTI E/I를 선택해주세요.")
        @Pattern(regexp = "^[EI]$", message = "MBTI E/I는 E, I 중 하나여야 합니다.")
        String mbti1,

        @NotNull(message = "MBTI N/S를 선택해주세요.")
        @Pattern(regexp = "^[NS]$", message = "MBTI N/S는 N, S 중 하나여야 합니다.")
        String mbti2,

        @NotNull(message = "MBTI T/F를 선택해주세요.")
        @Pattern(regexp = "^[TF]$", message = "MBTI T/F는 T, F 중 하나여야 합니다.")
        String mbti3,

        @NotNull(message = "MBTI J/P를 선택해주세요.")
        @Pattern(regexp = "^[JP]$", message = "MBTI J/P는 J, P 중 하나여야 합니다.")
        String mbti4,

        @NotNull(message = "1순위를 선택해주세요.")
        PreferenceCategory priority1,

        @NotNull(message = "2순위를 선택해주세요.")
        PreferenceCategory priority2,

        @NotNull(message = "3순위를 선택해주세요.")
        PreferenceCategory priority3
){}
