package masil.backend.modules.member.service;

import static masil.backend.modules.member.exception.MemberExceptionType.EMAIL_CODE_DOES_NOT_EXISTS;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import masil.backend.modules.member.entity.MemberEmailVerification;
import masil.backend.modules.member.exception.MemberException;
import masil.backend.modules.member.repository.MemberEmailVerificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberEmailVerificationLowService {
    private final MemberEmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteEmailVerification(final String email) {
        emailVerificationRepository.deleteByEmail(email);
        emailVerificationRepository.flush();
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
