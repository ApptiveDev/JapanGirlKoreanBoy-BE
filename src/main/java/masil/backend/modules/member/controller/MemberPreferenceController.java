package masil.backend.modules.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import masil.backend.global.security.annotation.LoginMember;
import masil.backend.global.security.dto.MemberDetails;
import masil.backend.modules.member.dto.request.SaveMemberPreferenceRequest;
import masil.backend.modules.member.dto.response.MemberPreferenceResponse;
import masil.backend.modules.member.service.MemberPreferenceHighService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/me/preferences")
@RequiredArgsConstructor
public class MemberPreferenceController {
    private final MemberPreferenceHighService memberPreferenceHighService;

    @PostMapping
    public ResponseEntity<Void> saveMemberPreference(
            @LoginMember MemberDetails memberDetails,
            @Valid @RequestBody final SaveMemberPreferenceRequest request
    ) {
        memberPreferenceHighService.saveMemberPreference(memberDetails.memberId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberPreferenceResponse> getMemberPreference(
            @LoginMember MemberDetails memberDetails
    ) {
        final MemberPreferenceResponse response = memberPreferenceHighService.getMemberPreference(memberDetails.memberId());
        return ResponseEntity.ok(response);
    }
}
