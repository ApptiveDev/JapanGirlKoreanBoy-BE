package masil.backend.modules.member.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import masil.backend.modules.member.enums.*;

/**
 * OAuth2 로그인 후 프로필 완성을 위한 요청 DTO
 */
public record CompleteOAuth2ProfileRequest(
        @NotNull(message = "성별을 입력해주세요.")
        Gender gender,
        
        @NotNull(message = "키를 입력해주세요.")
        @Min(value = 100, message = "키는 100cm 이상이어야 합니다.")
        Integer height,
        
        @NotNull(message = "몸무게를 입력해주세요.")
        @Min(value = 30, message = "몸무게는 30kg 이상이어야 합니다.")
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
        
        String otherInfo
) {}