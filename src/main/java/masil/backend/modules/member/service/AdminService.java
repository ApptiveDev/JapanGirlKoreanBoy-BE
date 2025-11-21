package masil.backend.modules.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.dto.request.ChangeMemberStatusRequest;
import masil.backend.modules.member.dto.request.CreateMatchingRequest;
import masil.backend.modules.member.dto.response.AdminMemberDetailResponse;
import masil.backend.modules.member.dto.response.AdminMemberListResponse;
import masil.backend.modules.member.dto.response.MatchingScoreResponse;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.Gender;
import masil.backend.modules.member.enums.MemberStatus;
import masil.backend.modules.member.exception.MemberException;
import masil.backend.modules.member.exception.MemberExceptionType;
import masil.backend.modules.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    
    private final MemberRepository memberRepository;
    private final MemberLowService memberLowService;
    private final MatchingScoreService matchingScoreService;
    

    //Use Case 1: 승인 대기 상태 유저 목록 조회

    @Transactional(readOnly = true)
    public List<AdminMemberListResponse> getPendingApprovalMembers(String keyword) {
        List<Member> members;
        
        if (keyword != null && !keyword.isBlank()) {
            members = memberRepository.findByStatusAndKeyword(MemberStatus.PENDING_APPROVAL, keyword);
        } else {
            members = memberRepository.findByStatus(MemberStatus.PENDING_APPROVAL);
        }
        
        log.info("승인 대기 유저 조회: {}명 (keyword: {})", members.size(), keyword);
        
        return members.stream()
                .map(AdminMemberListResponse::from)
                .collect(Collectors.toList());
    }
    

    //Use Case 1: 유저 상세 정보 조회
    
    @Transactional(readOnly = true)
    public AdminMemberDetailResponse getMemberDetail(Long memberId) {
        Member member = memberLowService.getValidateExistMemberById(memberId);
        log.info("유저 상세 정보 조회: memberId={}", memberId);
        return AdminMemberDetailResponse.from(member);
    }
    

    //Use Case 2: 승인대기 유저 상태 변경
    // 승인대기 → 연결중 또는 블랙유저

    public void changeMemberStatus(Long memberId, ChangeMemberStatusRequest request) {
        Member member = memberLowService.getValidateExistMemberById(memberId);
        
        // 승인 대기 상태가 아니면 에러
        if (member.getStatus() != MemberStatus.PENDING_APPROVAL) {
            log.warn("승인 대기 상태가 아닌 유저의 상태 변경 시도: memberId={}, 현재 상태={}", 
                    memberId, member.getStatus());
            throw new IllegalArgumentException("승인 대기 상태의 유저만 상태를 변경할 수 있습니다.");
        }
        
        // 연결중 또는 블랙유저로만 변경 가능
        if (request.status() != MemberStatus.CONNECTING && request.status() != MemberStatus.BLACKLISTED) {
            throw new IllegalArgumentException("승인 대기 상태의 유저는 '연결중' 또는 '블랙유저' 상태로만 변경할 수 있습니다.");
        }
        
        member.changeStatus(request.status());
        log.info("유저 상태 변경 완료: memberId={}, 변경 상태={}", memberId, request.status());
    }
    

    //Use Case 3: 연결중 상태 여성 유저 목록 조회

    @Transactional(readOnly = true)
    public List<AdminMemberListResponse> getConnectingFemaleMembers() {
        List<Member> members = memberRepository.findByGenderAndStatus(
                Gender.JAPANESE_FEMALE, 
                MemberStatus.CONNECTING
        );
        
        log.info("연결중 상태 여성 유저 조회: {}명", members.size());
        
        return members.stream()
                .map(AdminMemberListResponse::from)
                .collect(Collectors.toList());
    }
    

    //Use Case 4: 여성 유저 기준으로 매칭 가능한 남성 유저 목록 조회 (점수 내림차순)

    @Transactional(readOnly = true)
    public List<MatchingScoreResponse> getMatchingCandidates(Long femaleMemberId) {
        Member femaleMember = memberLowService.getValidateExistMemberById(femaleMemberId);
        
        // 여성 유저가 연결중 상태인지 확인
        if (femaleMember.getStatus() != MemberStatus.CONNECTING) {
            throw new IllegalArgumentException("연결중 상태의 여성 유저만 매칭 후보를 조회할 수 있습니다.");
        }
        
        // 여성이 맞는지 확인
        if (femaleMember.getGender() != Gender.JAPANESE_FEMALE) {
            throw new IllegalArgumentException("일본 여성 유저만 매칭 후보를 조회할 수 있습니다.");
        }
        
        // 연결중 상태 남성 유저 조회
        List<Member> maleMembers = memberRepository.findByGenderAndStatus(
                Gender.KOREAN_MALE,
                MemberStatus.CONNECTING
        );
        
        log.info("매칭 후보 조회: 여성 memberId={}, 남성 후보 수={}", femaleMemberId, maleMembers.size());
        
        // 매칭 점수 계산 및 정렬
        return maleMembers.stream()
                .map(male -> {
                    Double score = matchingScoreService.calculateMatchingScore(femaleMember, male);
                    return MatchingScoreResponse.from(male, score);
                })
                .sorted((a, b) -> Double.compare(b.matchingScore(), a.matchingScore())) // 내림차순
                .collect(Collectors.toList());
    }
    

    //Use Case 5: 최종 매칭 생성 (여성 1명 + 남성 3명을 연결됨 상태로 변경)

    public void createMatching(CreateMatchingRequest request) {
        // 여성 유저 조회 및 검증
        Member femaleMember = memberLowService.getValidateExistMemberById(request.femaleMemberId());
        
        if (femaleMember.getStatus() != MemberStatus.CONNECTING) {
            log.warn("연결중 상태가 아닌 여성 유저 매칭 시도: memberId={}, 상태={}", 
                    request.femaleMemberId(), femaleMember.getStatus());
            throw new IllegalArgumentException("연결중 상태의 여성 유저만 매칭할 수 있습니다.");
        }
        
        if (femaleMember.getGender() != Gender.JAPANESE_FEMALE) {
            throw new IllegalArgumentException("일본 여성 유저만 매칭할 수 있습니다.");
        }
        
        // 남성 유저들 조회 및 검증
        if (request.maleMemberIds().size() != 3) {
            throw new IllegalArgumentException("남성 유저는 정확히 3명을 선택해야 합니다.");
        }
        
        List<Member> maleMembers = request.maleMemberIds().stream()
                .map(memberLowService::getValidateExistMemberById)
                .peek(member -> {
                    if (member.getStatus() != MemberStatus.CONNECTING) {
                        log.warn("연결중 상태가 아닌 남성 유저 매칭 시도: memberId={}, 상태={}", 
                                member.getId(), member.getStatus());
                        throw new IllegalArgumentException(
                                String.format("선택한 유저 중 매칭 불가 상태가 있습니다. (memberId: %d, 상태: %s)", 
                                        member.getId(), member.getStatus()));
                    }
                    if (member.getGender() != Gender.KOREAN_MALE) {
                        throw new IllegalArgumentException(
                                String.format("한국 남성만 매칭할 수 있습니다. (memberId: %d)", member.getId()));
                    }
                })
                .collect(Collectors.toList());
        
        // 중복 체크
        long distinctCount = maleMembers.stream()
                .map(Member::getId)
                .distinct()
                .count();
        
        if (distinctCount != 3) {
            throw new IllegalArgumentException("중복된 남성 유저가 선택되었습니다.");
        }
        
        // 상태 변경: 연결중 → 연결됨
        femaleMember.changeToConnected();
        maleMembers.forEach(Member::changeToConnected);
        
        log.info("매칭 생성 완료: 여성 memberId={}, 남성 memberIds={}", 
                request.femaleMemberId(), 
                request.maleMemberIds());
        
        // TODO: 매칭 테이블에 매칭 기록 생성 (선택사항)
    }
}