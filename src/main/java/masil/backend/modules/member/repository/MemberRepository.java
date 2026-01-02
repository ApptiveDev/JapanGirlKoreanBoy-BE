package masil.backend.modules.member.repository;

import java.util.List;
import java.util.Optional;
import masil.backend.modules.member.entity.*;
import masil.backend.modules.member.enums.Gender;
import masil.backend.modules.member.enums.MemberStatus;
import masil.backend.modules.member.enums.Provider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(final String email);

    Boolean existsByEmail(final String email);

    Optional<Member> findByEmailAndProvider(String email, Provider provider);
    
    List<Member> findByStatus(MemberStatus status);
    //단일 상태 조회
    List<Member> findByGenderAndStatus(Gender gender, MemberStatus status);
    //여러 상태 조회
    List<Member> findByGenderAndStatusIn(Gender gender, List<MemberStatus> statuses);

    @Query("SELECT m FROM Member m WHERE m.status = :status " +
           "AND (m.name LIKE %:keyword% OR m.email LIKE %:keyword%)")
    List<Member> findByStatusAndKeyword(@Param("status") MemberStatus status, 
                                         @Param("keyword") String keyword);
}