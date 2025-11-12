package masil.backend.modules.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import masil.backend.modules.member.dto.request.SaveMemberPreferenceRequest;
import masil.backend.modules.member.dto.response.MemberPreferenceResponse;
import masil.backend.modules.member.service.MemberPreferenceHighService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/me/preferences")
@RequiredArgsConstructor
public class MemberPreferenceController {
    private final MemberPreferenceHighService memberPreferenceHighService;

    @PostMapping
    public ResponseEntity<Void> saveMemberPreference(
            @AuthenticationPrincipal final Long memberId,
            @Valid @RequestBody final SaveMemberPreferenceRequest request
    ) {
        memberPreferenceHighService.saveMemberPreference(memberId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberPreferenceResponse> getMemberPreference(
            @AuthenticationPrincipal final Long memberId
    ) {
        final MemberPreferenceResponse response = memberPreferenceHighService.getMemberPreference(memberId);
        return ResponseEntity.ok(response);
    }
}
