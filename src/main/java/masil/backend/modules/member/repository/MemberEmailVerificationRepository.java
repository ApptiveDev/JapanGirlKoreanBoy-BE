package masil.backend.modules.member.repository;

import masil.backend.modules.member.entity.MemberEmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberEmailVerificationRepository extends JpaRepository<MemberEmailVerification, Long> {

    Optional<MemberEmailVerification> findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}
