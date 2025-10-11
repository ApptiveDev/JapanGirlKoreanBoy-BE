package masil.backend.modules.member.service;

import static masil.backend.modules.member.exception.MemberExceptionType.ALREADY_EXIST_EMAIL;
import static masil.backend.modules.member.exception.MemberExceptionType.NOT_FOUND_MEMBER;

import lombok.RequiredArgsConstructor;
import masil.backend.modules.member.entity.*;
import masil.backend.modules.member.enums.Provider;
import masil.backend.modules.member.dto.response.OAuth2UserInfo;
import masil.backend.modules.member.exception.MemberException;
import masil.backend.modules.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void saveMember(final String name, final String email, final String password) {
        final Member member = Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .provider(Provider.LOCAL)
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

    public Member getValidateExistMemberByEmail(final String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

    public Member getValidateExistMemberById(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

    //이메일과 제공자로 회원 조회, 기존 회원 여부 판단
    public Member findByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findByEmailAndProvider(email, provider).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }
}
