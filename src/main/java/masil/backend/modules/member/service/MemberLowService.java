package masil.backend.modules.member.service;

import static masil.backend.modules.member.exception.MemberExceptionType.ALREADY_EXIST_EMAIL;
import static masil.backend.modules.member.exception.MemberExceptionType.NOT_FOUND_MEMBER;

import lombok.RequiredArgsConstructor;
import masil.backend.modules.member.entity.*;
import masil.backend.modules.member.enums.Asset;
import masil.backend.modules.member.enums.DrinkingFrequency;
import masil.backend.modules.member.enums.Education;
import masil.backend.modules.member.enums.Gender;
import masil.backend.modules.member.enums.Provider;
import masil.backend.modules.member.dto.response.OAuth2UserInfo;
import masil.backend.modules.member.enums.Religion;
import masil.backend.modules.member.enums.SmokingStatus;
import masil.backend.modules.member.exception.MemberException;
import masil.backend.modules.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import masil.backend.modules.member.dto.OAuth2TempUserInfo;
import masil.backend.modules.member.dto.request.CompleteOAuth2ProfileRequest;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberLowService {
    private final MemberRepository memberRepository;

    public void checkIsDuplicateEmail(final String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new MemberException(ALREADY_EXIST_EMAIL);
        }
    }

    @Transactional
    public void saveLocalMember(
            final String name,
            final String email,
            final String password,
            final Gender gender,
            final Integer height,
            final Integer weight,
            final String residenceArea,
            final SmokingStatus smokingStatus,
            final DrinkingFrequency drinkingFrequency,
            final Religion religion,
            final String religionOther,
            final Education education,
            final Asset asset,
            final String otherInfo,
            final String profileImageUrl
    ) {
        final Member member = Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .provider(Provider.LOCAL)
                .gender(gender)
                .height(height)
                .weight(weight)
                .residenceArea(residenceArea)
                .smokingStatus(smokingStatus)
                .drinkingFrequency(drinkingFrequency)
                .religion(religion)
                .religionOther(religionOther)
                .education(education)
                .asset(asset)
                .otherInfo(otherInfo)
                .profileImageUrl(profileImageUrl)
                .build();

        memberRepository.save(member);
    }

    //OAuth2 신규 회원 저장
    @Transactional
    public Member saveOAuth2Member(OAuth2UserInfo userInfo) {
        final Member member = Member.builder()
                .name(userInfo.name())
                .email(userInfo.email())
                .provider(Provider.GOOGLE)
                .providerId(userInfo.providerId())
                .build();

        return memberRepository.save(member);
    }
    //OAuth2 프로필 완성 API에서 사용
    @Transactional
    public Member saveOAuth2MemberWithProfile(
           OAuth2TempUserInfo tempUserInfo,
           CompleteOAuth2ProfileRequest profileRequest
   ) {
       final Member member = Member.builder()
               .name(tempUserInfo.name())
               .email(tempUserInfo.email())
               .provider(Provider.GOOGLE)
               .providerId(tempUserInfo.providerId())
               .gender(profileRequest.gender())
               .height(profileRequest.height())
               .weight(profileRequest.weight())
               .residenceArea(profileRequest.residenceArea())
               .smokingStatus(profileRequest.smokingStatus())
               .drinkingFrequency(profileRequest.drinkingFrequency())
               .religion(profileRequest.religion())
               .education(profileRequest.education())
               .asset(profileRequest.asset())
               .otherInfo(profileRequest.otherInfo())
               .profileImageUrl(tempUserInfo.profileImageUrl())
               .build();
   
       return memberRepository.save(member);
   }

    public Member getValidateExistMemberByEmail(final String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

    public Member getValidateExistMemberById(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }
    //이메일과 제공자로 회원 조회, 기존 회원 여부 판단
    public Member findByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findByEmailAndProvider(email, provider).orElse(null); // 신규회원인 경우 null 반환
    }
}
