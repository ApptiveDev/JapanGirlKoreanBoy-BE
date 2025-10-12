package masil.backend.modules.member.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import masil.backend.modules.member.enums.Asset;
import masil.backend.modules.member.enums.DrinkingFrequency;
import masil.backend.modules.member.enums.Education;
import masil.backend.modules.member.enums.Gender;
import masil.backend.modules.member.enums.Religion;
import masil.backend.modules.member.enums.SmokingStatus;

public record SignUpRequest(
    @NotNull(message = "이름을 입력해주세요.")
    String name,

    @NotNull(message = "이메일을 입력해주세요.")
    String email,

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&~])[A-Za-z\\d@$!%*#?&~]{8,16}$",
            message = "비밀번호는 8~16자여야 하며, 영문·숫자·특수문자를 모두 포함해야 합니다."
    )
    String password,

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

    Education education,

    Asset asset,

    String otherInfo,

    String profileImageUrl
){}
