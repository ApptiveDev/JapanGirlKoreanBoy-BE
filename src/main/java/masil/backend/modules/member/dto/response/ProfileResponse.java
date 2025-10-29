package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.*;

public record ProfileResponse(
        Long memberId,
        String name,
        String email,
        Gender gender,
        Integer height,
        Integer weight,
        String residenceArea,
        SmokingStatus smokingStatus,
        DrinkingFrequency drinkingFrequency,
        Religion religion,
        Education education,
        Asset asset,
        String otherInfo,
        String profileImageUrl
) {
    public ProfileResponse(final Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getGender(),
                member.getHeight(),
                member.getWeight(),
                member.getResidenceArea(),
                member.getSmokingStatus(),
                member.getDrinkingFrequency(),
                member.getReligion(),
                member.getEducation(),
                member.getAsset(),
                member.getOtherInfo(),
                member.getProfileImageUrl()
        );
    }
}