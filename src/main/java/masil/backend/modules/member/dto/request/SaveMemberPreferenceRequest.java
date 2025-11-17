package masil.backend.modules.member.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import masil.backend.modules.member.enums.AppearanceStyle;
import masil.backend.modules.member.enums.EducationLevel;
import masil.backend.modules.member.enums.JobType;
import masil.backend.modules.member.enums.ParentAssetLevel;
import masil.backend.modules.member.enums.PreferenceCategory;
import masil.backend.modules.member.enums.Religion;

import java.util.List;

public record SaveMemberPreferenceRequest(
        @Min(value = 130, message = "선호 키 최소값은 130 이상이어야 합니다.")
        @Max(value = 230, message = "선호 키 최소값은 230 이하여야 합니다.")
        Integer preferredHeightMin,

        @Min(value = 130, message = "선호 키 최대값은 130 이상이어야 합니다.")
        @Max(value = 230, message = "선호 키 최대값은 230 이하여야 합니다.")
        Integer preferredHeightMax,

        List<Religion> avoidReligions,

        EducationLevel preferredEducationLevel,

        AppearanceStyle preferredAppearanceStyle,

        ParentAssetLevel parentAssetRequirement,

        @Min(value = 0, message = "선호 자산 최소값은 0 이상이어야 합니다.")
        Long preferredAssetMin,

        @Min(value = 0, message = "선호 자산 최대값은 0 이상이어야 합니다.")
        Long preferredAssetMax,

        @Size(max = 3, message = "선호 직업은 최대 3개까지 선택 가능합니다.")
        List<JobType> preferredJobs,

        @Size(max = 3, message = "비선호 직업은 최대 3개까지 선택 가능합니다.")
        List<JobType> avoidedJobs,

        @Pattern(regexp = "^[EIX]$", message = "MBTI E/I는 E, I, X 중 하나여야 합니다.")
        String mbti1,

        @Pattern(regexp = "^[NSX]$", message = "MBTI N/S는 N, S, X 중 하나여야 합니다.")
        String mbti2,

        @Pattern(regexp = "^[TFX]$", message = "MBTI T/F는 T, F, X 중 하나여야 합니다.")
        String mbti3,

        @Pattern(regexp = "^[JPX]$", message = "MBTI J/P는 J, P, X 중 하나여야 합니다.")
        String mbti4,

        @NotNull(message = "1순위를 선택해주세요.")
        PreferenceCategory priority1,

        @NotNull(message = "2순위를 선택해주세요.")
        PreferenceCategory priority2,

        @NotNull(message = "3순위를 선택해주세요.")
        PreferenceCategory priority3
) {}
