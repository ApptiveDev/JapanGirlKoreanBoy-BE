package masil.backend.modules.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.dto.response.FemaleMatchingListResponse;
import masil.backend.modules.member.dto.response.MaleMatchingNotificationResponse;
import masil.backend.modules.member.entity.Matching;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.Gender;
import masil.backend.modules.member.enums.MatchingStatus;
import masil.backend.modules.member.repository.MatchingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {
    
    private final MatchingRepository matchingRepository;
    private final MemberLowService memberLowService;
    
    
    //여성에게 매칭된 남성 목록 조회
    @Transactional(readOnly = true)
    public List<FemaleMatchingListResponse> getFemaleMatchingList(Long femaleMemberId) {
        Member femaleMember = memberLowService.getValidateExistMemberById(femaleMemberId);
        
        if (femaleMember.getGender() != Gender.JAPANESE_FEMALE) {
            throw new IllegalArgumentException("일본 여성 유저만 매칭 목록을 조회할 수 있습니다.");
        }
        List<Matching> matchings = matchingRepository.findByFemaleMemberIdAndStatusOrderByMatchingOrder(
                femaleMemberId, 
                MatchingStatus.PENDING_FEMALE_SELECTION
        );
        return FemaleMatchingListResponse.fromList(matchings);
    }
    
    //여성이 남성 1명 선택
    public void selectMaleByFemale(Long femaleMemberId, Long matchingId) {
        Member femaleMember = memberLowService.getValidateExistMemberById(femaleMemberId);
        
        if (femaleMember.getGender() != Gender.JAPANESE_FEMALE) {
            throw new IllegalArgumentException("일본 여성 유저만 매칭을 선택할 수 있습니다.");
        }
        
        // 매칭 조회 및 검증
        Matching selectedMatching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭을 찾을 수 없습니다."));
        
        if (!selectedMatching.getFemaleMember().getId().equals(femaleMemberId)) {
            throw new IllegalArgumentException("본인의 매칭만 선택할 수 있습니다.");
        }
        
        if (selectedMatching.getStatus() != MatchingStatus.PENDING_FEMALE_SELECTION) {
            throw new IllegalArgumentException("선택 대기 중인 매칭만 선택할 수 있습니다.");
        }
        
        // 같은 여성의 다른 매칭들을 먼저 조회 (선택 전에 조회해야 함)
        List<Matching> otherMatchings = matchingRepository.findByFemaleMemberIdAndStatusOrderByMatchingOrder(
                femaleMemberId,
                MatchingStatus.PENDING_FEMALE_SELECTION
        );
        
        // 선택된 매칭을 수락 대기 상태로 변경
        selectedMatching.selectByFemale();
        
        // 같은 여성의 다른 매칭들을 거절 상태로 변경
        otherMatchings.forEach(matching -> {
            if (!matching.getId().equals(matchingId)) {
                matching.reject();
            }
        });
        
        log.info("여성이 남성 선택: femaleMemberId={}, matchingId={}, selectedMaleId={}, 거절된 매칭 수={}", 
                femaleMemberId, matchingId, selectedMatching.getMaleMember().getId(), 
                otherMatchings.size() - 1);
    }
    
    //남성에게 매칭 알림 조회 (수락 대기 중인 매칭)
    @Transactional(readOnly = true)
    public List<MaleMatchingNotificationResponse> getMaleMatchingNotifications(Long maleMemberId) {
        // 남성 유저 검증
        Member maleMember = memberLowService.getValidateExistMemberById(maleMemberId);
        
        if (maleMember.getGender() != Gender.KOREAN_MALE) {
            throw new IllegalArgumentException("한국 남성 유저만 매칭 알림을 조회할 수 있습니다.");
        }
        
        // 수락 대기 중인 매칭 조회
        List<Matching> matchings = matchingRepository.findByMaleMemberIdAndStatus(
                maleMemberId,
                MatchingStatus.PENDING_MALE_ACCEPTANCE
        );
        
        log.info("남성 매칭 알림 조회: maleMemberId={}, 알림 수={}", maleMemberId, matchings.size());
        
        return matchings.stream()
                .map(MaleMatchingNotificationResponse::from)
                .toList();
    }
    
    //남성이 매칭 수락
    public void acceptMatchingByMale(Long maleMemberId, Long matchingId) {
        // 남성 유저 검증
        Member maleMember = memberLowService.getValidateExistMemberById(maleMemberId);
        
        if (maleMember.getGender() != Gender.KOREAN_MALE) {
            throw new IllegalArgumentException("한국 남성 유저만 매칭을 수락할 수 있습니다.");
        }
        
        // 매칭 조회 및 검증
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭을 찾을 수 없습니다."));
        
        if (!matching.getMaleMember().getId().equals(maleMemberId)) {
            throw new IllegalArgumentException("본인의 매칭만 수락할 수 있습니다.");
        }
        
        if (matching.getStatus() != MatchingStatus.PENDING_MALE_ACCEPTANCE) {
            throw new IllegalArgumentException("수락 대기 중인 매칭만 수락할 수 있습니다.");
        }
        
        // 같은 남성의 다른 매칭들을 먼저 조회 (수락 전에 조회해야 함)
        List<Matching> otherMatchings = matchingRepository.findByMaleMemberIdAndStatus(
                maleMemberId,
                MatchingStatus.PENDING_MALE_ACCEPTANCE
        );
        
        // 매칭 수락
        matching.acceptByMale();
        
        // 같은 남성의 다른 매칭들을 거절 상태로 변경
        otherMatchings.forEach(otherMatching -> {
            if (!otherMatching.getId().equals(matchingId)) {
                otherMatching.reject();
            }
        });
        
        log.info("남성이 매칭 수락: maleMemberId={}, matchingId={}, femaleMemberId={}, 거절된 매칭 수={}", 
                maleMemberId, matchingId, matching.getFemaleMember().getId(), 
                otherMatchings.size() - 1);
    }
    
    //남성이 매칭 거절
    public void rejectMatchingByMale(Long maleMemberId, Long matchingId) {
        // 남성 유저 검증
        Member maleMember = memberLowService.getValidateExistMemberById(maleMemberId);
        
        if (maleMember.getGender() != Gender.KOREAN_MALE) {
            throw new IllegalArgumentException("한국 남성 유저만 매칭을 거절할 수 있습니다.");
        }
        
        // 매칭 조회 및 검증
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭을 찾을 수 없습니다."));
        
        if (!matching.getMaleMember().getId().equals(maleMemberId)) {
            throw new IllegalArgumentException("본인의 매칭만 거절할 수 있습니다.");
        }
        
        if (matching.getStatus() != MatchingStatus.PENDING_MALE_ACCEPTANCE) {
            throw new IllegalArgumentException("수락 대기 중인 매칭만 거절할 수 있습니다.");
        }
        
        // 매칭 거절
        matching.reject();
        
        log.info("남성이 매칭 거절: maleMemberId={}, matchingId={}, femaleMemberId={}", 
                maleMemberId, matchingId, matching.getFemaleMember().getId());
    }

    //여성이 선택한 매칭의 상태 조회 (수락 대기, 수락됨, 거절됨)
    @Transactional(readOnly = true)
    public List<FemaleMatchingListResponse> getSelectedMatchingStatus(Long femaleMemberId) {
        // 여성 유저 검증
        Member femaleMember = memberLowService.getValidateExistMemberById(femaleMemberId);
        
        if (femaleMember.getGender() != Gender.JAPANESE_FEMALE) {
            throw new IllegalArgumentException("일본 여성 유저만 매칭 상태를 조회할 수 있습니다.");
        }
        
        // 선택된 매칭 조회 (수락 대기, 수락됨, 거절됨 상태)
        List<MatchingStatus> statuses = List.of(
                MatchingStatus.PENDING_MALE_ACCEPTANCE,
                MatchingStatus.ACCEPTED,
                MatchingStatus.REJECTED
        );
        
        List<Matching> matchings = matchingRepository.findByFemaleMemberIdAndStatusIn(
                femaleMemberId,
                statuses
        );
        
        log.info("여성 선택 매칭 상태 조회: femaleMemberId={}, 매칭 수={}", femaleMemberId, matchings.size());
        
        return FemaleMatchingListResponse.fromList(matchings);
    }
}

