package masil.backend.modules.member.repository;

import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.entity.MemberPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberPreferenceRepository extends JpaRepository<MemberPreference, Long> {
    Optional<MemberPreference> findByMember(Member member);

    Optional<MemberPreference> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
