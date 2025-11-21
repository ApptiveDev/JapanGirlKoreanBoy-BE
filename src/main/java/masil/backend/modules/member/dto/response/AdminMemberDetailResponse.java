package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.*;

import java.time.LocalDateTime;

public record AdminMemberDetailResponse(
        Long id,
        String name,
        String email,
        MemberStatus status,
        Gender gender,
        Integer height,
        Integer weight,
        String residenceArea,
        SmokingStatus smokingStatus,
        DrinkingFrequency drinkingFrequency,
        Religion religion,
        String religionOther,
        Education education,
        Asset asset,
        String otherInfo,
        String profileImageUrl,
        LocalDateTime createdAt
) {
    public static AdminMemberDetailResponse from(Member member) {
        return new AdminMemberDetailResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getStatus(),
                member.getGender(),
                member.getHeight(),
                member.getWeight(),
                member.getResidenceArea(),
                member.getSmokingStatus(),
                member.getDrinkingFrequency(),
                member.getReligion(),
                member.getReligionOther(),
                member.getEducation(),
                member.getAsset(),
                member.getOtherInfo(),
                member.getProfileImageUrl(),
                member.getCreatedAt()
        );
    }
}