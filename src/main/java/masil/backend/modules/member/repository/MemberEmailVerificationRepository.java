package masil.backend.modules.member.repository;

import masil.backend.modules.member.entity.MemberEmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberEmailVerificationRepository extends JpaRepository<MemberEmailVerification, Long> {

    /**
     * 이메일로 인증 정보 조회
     */
    Optional<MemberEmailVerification> findByEmail(String email);

    /**
     * 이메일로 인증 정보 삭제 (재발송 시 사용)
     */
    void deleteByEmail(String email);

    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);
}
