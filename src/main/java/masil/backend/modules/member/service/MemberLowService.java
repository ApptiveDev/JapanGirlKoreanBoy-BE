package masil.backend.modules.member.service;

import static masil.backend.modules.member.exception.MemberExceptionType.ALREADY_EXIST_EMAIL;
import static masil.backend.modules.member.exception.MemberExceptionType.EMAIL_CODE_DOES_NOT_EXISTS;
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
import masil.backend.modules.member.repository.MemberEmailVerificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import masil.backend.modules.member.dto.OAuth2TempUserInfo;
import masil.backend.modules.member.dto.request.CompleteOAuth2ProfileRequest;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberLowService {
    private final MemberRepository memberRepository;
    private final MemberEmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    public void checkIsDuplicateEmail(final String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new MemberException(ALREADY_EXIST_EMAIL);
        }
    }

    @Transactional
    public void saveLocalMember(
            final String name, final String email, final String password,
            final Gender gender, final Integer height, final Integer weight,
            final String residenceArea, final SmokingStatus smokingStatus, final DrinkingFrequency drinkingFrequency,
            final Religion religion, final String religionOther, final Education education,
            final Asset asset, final String otherInfo, final String profileImageUrl
    ) {
        final Member member = Member.builder()
                .name(name).email(email).password(password)
                .provider(Provider.LOCAL).gender(gender).height(height)
                .weight(weight).residenceArea(residenceArea).smokingStatus(smokingStatus)
                .drinkingFrequency(drinkingFrequency).religion(religion).religionOther(religionOther)
                .education(education).asset(asset).otherInfo(otherInfo).profileImageUrl(profileImageUrl)
                .build();

        memberRepository.save(member);
    }

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

    public Member findByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findByEmailAndProvider(email, provider).orElse(null);
    }

    @Transactional
    public void saveEmailVerification(final String email, final String code, final LocalDateTime expiresAt) {
        final MemberEmailVerification verification = MemberEmailVerification.builder()
                .email(email)
                .code(code)
                .expiresAt(expiresAt)
                .build();

        emailVerificationRepository.save(verification);
    }

    public MemberEmailVerification findEmailVerificationByEmail(final String email) {
        return emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(EMAIL_CODE_DOES_NOT_EXISTS));
    }

    public boolean existsEmailVerification(final String email) {
        return emailVerificationRepository.existsByEmail(email);
    }

    @Transactional
    public void deleteEmailVerification(final String email) {
        emailVerificationRepository.deleteByEmail(email);
    }

    public void sendEmail(final String to, final String code) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[이어드림] 회원가입 이메일 인증 코드");
        message.setText(
                "안녕하세요, 이어드림입니다.\n\n" +
                        "회원가입을 위한 인증 코드는 다음과 같습니다:\n\n" +
                        "[" + code + "]\n\n" +
                        "인증 코드는 5분간 유효합니다.\n" +
                        "본인이 요청하지 않았다면 이 메일을 무시해주세요."
        );

        mailSender.send(message);
    }
}
