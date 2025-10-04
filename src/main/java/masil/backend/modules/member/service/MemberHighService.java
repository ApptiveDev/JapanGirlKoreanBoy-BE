package masil.backend.modules.member.service;

import static masil.backend.modules.member.exception.MemberExceptionType.CANNOT_MATCH_PASSWORD;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.global.security.provider.JwtProvider;
import masil.backend.modules.member.dto.request.SignInRequest;
import masil.backend.modules.member.dto.request.SignUpRequest;
import masil.backend.modules.member.dto.response.SignInResponse;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.exception.MemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberHighService {
    private final MemberLowService memberLowService;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public void signUp(final SignUpRequest signUpRequest) {
        final String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        memberLowService.checkIsDuplicateEmail(signUpRequest.email());

        final Member savedMember = memberLowService.saveMember(
                signUpRequest.name(),
                signUpRequest.email(),
                encodedPassword
        );

        getToken(savedMember.getId(), savedMember.getName());
    }

    public SignInResponse signIn(final SignInRequest signInRequest) {
        final Member member =  memberLowService.getValidateExistMemberByEmail(signInRequest.email());
        checkCorrectPassword(member.getPassword(), signInRequest.password());
        final String token = getToken(member.getId(), member.getName());
        return new SignInResponse(member, token);
    }

    private void checkCorrectPassword(final String savePassword, final String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, savePassword)) {
            throw new MemberException(CANNOT_MATCH_PASSWORD);
        }
    }

    private String getToken(final Long memberId, final String name) {
        return jwtProvider.createToken(String.valueOf(memberId), name);
    }
}
