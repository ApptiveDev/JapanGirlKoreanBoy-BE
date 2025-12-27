package masil.backend.modules.adminMember.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.adminMember.dto.request.ChangeMemberStatusRequest;
import masil.backend.modules.adminMember.dto.request.CreateMatchingRequest;
import masil.backend.modules.adminMember.dto.response.AdminMemberDetailResponse;
import masil.backend.modules.adminMember.dto.response.AdminMemberListResponse;
import masil.backend.modules.member.dto.response.MatchingScoreResponse;
import masil.backend.modules.adminMember.service.AdminMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import masil.backend.modules.member.dto.response.MatchedMemberListResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {
    
    private final AdminMemberService adminMemberService;
    
    @GetMapping("/pending-approval")
    public ResponseEntity<List<AdminMemberListResponse>> getPendingApprovalMembers(
            @RequestParam(required = false) String keyword
    ) {
        log.info("승인 대기 유저 목록 조회 요청: keyword={}", keyword);
        List<AdminMemberListResponse> members = adminMemberService.getPendingApprovalMembers(keyword);
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdminMemberDetailResponse> getMemberDetail(@PathVariable Long id) {
        log.info("유저 상세 정보 조회 요청: memberId={}", id);
        AdminMemberDetailResponse response = adminMemberService.getMemberDetail(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeMemberStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeMemberStatusRequest request
    ) {
        log.info("유저 상태 변경 요청: memberId={}, newStatus={}", id, request.status());
        adminMemberService.changeMemberStatus(id, request);
        return ResponseEntity.ok().build();
    }
    

    @GetMapping("/connecting/females")
    public ResponseEntity<List<AdminMemberListResponse>> getConnectingFemaleMembers() {
        log.info("승인완료 상태 여성 유저 목록 조회 요청");
        List<AdminMemberListResponse> members = adminMemberService.getConnectingFemaleMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{femaleId}/matching-candidates")
    public ResponseEntity<List<MatchingScoreResponse>> getMatchingCandidates(
            @PathVariable Long femaleId
    ) {
        log.info("매칭 후보 조회 요청: femaleMemberId={}", femaleId);
        List<MatchingScoreResponse> candidates = adminMemberService.getMatchingCandidates(femaleId);
        return ResponseEntity.ok(candidates);
    }

    @PostMapping("/matching")
    public ResponseEntity<Void> createMatching(
            @Valid @RequestBody CreateMatchingRequest request
    ) {
        log.info("매칭 생성 요청: femaleMemberId={}, maleMemberIds={}", 
                request.femaleMemberId(), request.maleMemberIds());
        adminMemberService.createMatching(request);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/matchings")
    public ResponseEntity<List<MatchedMemberListResponse>> getAllMatchings() {
        log.info("매칭 목록 조회 요청");
        List<MatchedMemberListResponse> matchings = adminMemberService.getAllMatchings();
        return ResponseEntity.ok(matchings);
    }
}
