package masil.backend.modules.member.repository;

import masil.backend.modules.member.entity.Matching;
import masil.backend.modules.member.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
    // 특정 여성과 매칭된 모든 남성 조회
    List<Matching> findByFemaleMemberIdOrderByMatchingOrder(Long femaleMemberId);
    
    // 특정 여성과 특정 순서의 매칭 조회
    Optional<Matching> findByFemaleMemberIdAndMatchingOrder(Long femaleMemberId, Integer matchingOrder);
    
    // 모든 매칭 조회 (여성별로 그룹화)
    @Query("SELECT DISTINCT m.femaleMember.id FROM Matching m ORDER BY m.femaleMember.id")
    List<Long> findDistinctFemaleMemberIds();
    
    // 특정 남성이 포함된 매칭 조회
    List<Matching> findByMaleMemberId(Long maleMemberId);
    
    // 특정 여성과 남성의 매칭 조회
    Optional<Matching> findByFemaleMemberIdAndMaleMemberId(Long femaleMemberId, Long maleMemberId);
    
    // 특정 여성의 선택 대기 중인 매칭 조회
    List<Matching> findByFemaleMemberIdAndStatusOrderByMatchingOrder(Long femaleMemberId, MatchingStatus status);
    
    // 특정 남성의 수락 대기 중인 매칭 조회
    List<Matching> findByMaleMemberIdAndStatus(Long maleMemberId, MatchingStatus status);
    
    // 특정 여성의 선택된 매칭 조회 (수락 대기 또는 수락됨)
    @Query("SELECT m FROM Matching m WHERE m.femaleMember.id = :femaleMemberId " +
           "AND m.status IN :statuses ORDER BY m.matchingOrder")
    List<Matching> findByFemaleMemberIdAndStatusIn(@Param("femaleMemberId") Long femaleMemberId, 
                                                    @Param("statuses") List<MatchingStatus> statuses);
}