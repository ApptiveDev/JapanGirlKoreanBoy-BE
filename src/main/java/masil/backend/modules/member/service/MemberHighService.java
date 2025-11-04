package masil.backend.modules.member.service;

import static masil.backend.modules.member.exception.MemberExceptionType.CANNOT_MATCH_PASSWORD;
import static masil.backend.modules.member.exception.MemberExceptionType.EMAIL_CODE_EXPIRED;
import static masil.backend.modules.member.exception.MemberExceptionType.EMAIL_CODE_NOT_MATCH;
import static masil.backend.modules.member.exception.MemberExceptionType.MEMBER_RELIGION_OTHER_FAILED;

import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.global.security.provider.JwtProvider;
import masil.backend.modules.member.dto.request.SignInRequest;
import masil.backend.modules.member.dto.request.SignUpRequest;
import masil.backend.modules.member.dto.response.MyInfoResponse;
import masil.backend.modules.member.dto.response.SignInResponse;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.entity.MemberEmailVerification;
import masil.backend.modules.member.enums.Religion;
import masil.backend.modules.member.exception.MemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberHighService {
    private final MemberLowService memberLowService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public void signUp(final SignUpRequest signUpRequest) {
        validateReligionOther(signUpRequest.religion(), signUpRequest.religionOther());

        final String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        memberLowService.checkIsDuplicateEmail(signUpRequest.email());

        memberLowService.saveLocalMember(
                signUpRequest.name(),
                signUpRequest.email(),
                encodedPassword,
                signUpRequest.gender(),
                signUpRequest.height(),
                signUpRequest.weight(),
                signUpRequest.residenceArea(),
                signUpRequest.smokingStatus(),
                signUpRequest.drinkingFrequency(),
                signUpRequest.religion(),
                signUpRequest.religionOther(),
                signUpRequest.education(),
                signUpRequest.asset(),
                signUpRequest.otherInfo(),
                signUpRequest.profileImageUrl()
        );
    }

    public SignInResponse signIn(final SignInRequest signInRequest) {
        final Member member =  memberLowService.getValidateExistMemberByEmail(signInRequest.email());
        checkCorrectPassword(member.getPassword(), signInRequest.password());
        final String token = getToken(member.getId(), member.getName());
        return new SignInResponse(member, token);
    }

    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(final Long memberId) {
        final Member member = memberLowService.getValidateExistMemberById(memberId);
        return new MyInfoResponse(member);
    }

    private void checkCorrectPassword(final String savePassword, final String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, savePassword)) {
            throw new MemberException(CANNOT_MATCH_PASSWORD);
        }
    }

    private String getToken(final Long memberId, final String name) {
        return jwtProvider.createToken(String.valueOf(memberId), name);
    }

    private void validateReligionOther(final Religion religion, final String religionOther) {
        if (religion == Religion.OTHER && (religionOther == null || religionOther.isBlank())) {
            throw new MemberException(MEMBER_RELIGION_OTHER_FAILED);
        }
    }
}
