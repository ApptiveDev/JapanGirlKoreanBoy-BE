package masil.backend.modules.member.service;

import static masil.backend.modules.member.exception.MemberExceptionType.EMAIL_CODE_EXPIRED;
import static masil.backend.modules.member.exception.MemberExceptionType.EMAIL_CODE_NOT_MATCH;
import static masil.backend.modules.member.exception.MemberExceptionType.INVALID_EMAIL;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import masil.backend.modules.member.entity.MemberEmailVerification;
import masil.backend.modules.member.exception.MemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberEmailVerificationHighService {
    private final MemberEmailVerificationLowService memberEmailVerificationLowService;
    private final MemberLowService memberLowService;
    private final EmailValidationService emailValidationService;

    private static final SecureRandom random = new SecureRandom();

    public void sendVerificationCode(final String email) {
        // 1. 이미 가입된 이메일인지 확인
        memberLowService.checkIsDuplicateEmail(email);

        // 2. 실제 존재하는 이메일인지 확인
        if (!emailValidationService.isEmailValid(email)) {
            throw new MemberException(INVALID_EMAIL);
        }

        // 3. 기존 인증 코드가 있으면 삭제
        if (memberEmailVerificationLowService.existsEmailVerification(email)) {
            memberEmailVerificationLowService.deleteEmailVerification(email);
        }

        // 4. 6자리 랜덤 코드 생성
        final String code = generateCode();

        // 5. 만료 시간 설정 (5분 후)
        final LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

        // 6. DB에 저장
        memberEmailVerificationLowService.saveEmailVerification(email, code, expiresAt);

        // 7. 이메일 발송
        memberEmailVerificationLowService.sendEmail(email, code);
    }

    public void verifyEmailCode(final String email, final String inputCode) {
        // 1. 인증 정보 조회
        final MemberEmailVerification verification = memberEmailVerificationLowService.findEmailVerificationByEmail(email);

        // 2. 만료 시간 체크
        if (verification.isExpired()) {
            throw new MemberException(EMAIL_CODE_EXPIRED);
        }

        // 3. 코드 일치 여부 확인
        if (!verification.isCodeMatch(inputCode)) {
            throw new MemberException(EMAIL_CODE_NOT_MATCH);
        }

        // 4. 인증 완료 처리
        verification.verify();

        // 5. 인증 코드 삭제
        memberEmailVerificationLowService.deleteEmailVerification(email);
    }

    private String generateCode() {
        final int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
