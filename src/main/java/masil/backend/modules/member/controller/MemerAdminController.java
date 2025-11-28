package masil.backend.modules.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.dto.request.ChangeMemberStatusRequest;
import masil.backend.modules.member.dto.request.CreateMatchingRequest;
import masil.backend.modules.member.dto.response.AdminMemberDetailResponse;
import masil.backend.modules.member.dto.response.AdminMemberListResponse;
import masil.backend.modules.member.dto.response.MatchingScoreResponse;
import masil.backend.modules.member.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class MemerAdminController {
    
    private final AdminService adminService;
    
    @GetMapping("/pending-approval")
    public ResponseEntity<List<AdminMemberListResponse>> getPendingApprovalMembers(
            @RequestParam(required = false) String keyword
    ) {
        log.info("승인 대기 유저 목록 조회 요청: keyword={}", keyword);
        List<AdminMemberListResponse> members = adminService.getPendingApprovalMembers(keyword);
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdminMemberDetailResponse> getMemberDetail(@PathVariable Long id) {
        log.info("유저 상세 정보 조회 요청: memberId={}", id);
        AdminMemberDetailResponse response = adminService.getMemberDetail(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeMemberStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeMemberStatusRequest request
    ) {
        log.info("유저 상태 변경 요청: memberId={}, newStatus={}", id, request.status());
        adminService.changeMemberStatus(id, request);
        return ResponseEntity.ok().build();
    }
    

    @GetMapping("/connecting/females")
    public ResponseEntity<List<AdminMemberListResponse>> getConnectingFemaleMembers() {
        log.info("승인완료 상태 여성 유저 목록 조회 요청");
        List<AdminMemberListResponse> members = adminService.getConnectingFemaleMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{femaleId}/matching-candidates")
    public ResponseEntity<List<MatchingScoreResponse>> getMatchingCandidates(
            @PathVariable Long femaleId
    ) {
        log.info("매칭 후보 조회 요청: femaleMemberId={}", femaleId);
        List<MatchingScoreResponse> candidates = adminService.getMatchingCandidates(femaleId);
        return ResponseEntity.ok(candidates);
    }

    @PostMapping("/matching")
    public ResponseEntity<Void> createMatching(
            @Valid @RequestBody CreateMatchingRequest request
    ) {
        log.info("매칭 생성 요청: femaleMemberId={}, maleMemberIds={}", 
                request.femaleMemberId(), request.maleMemberIds());
        adminService.createMatching(request);
        return ResponseEntity.ok().build();
    }
}