package masil.backend.modules.member.repository;

import java.util.Optional;
import masil.backend.modules.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(final String email);

    Boolean existsByEmail(final String Email);
}
