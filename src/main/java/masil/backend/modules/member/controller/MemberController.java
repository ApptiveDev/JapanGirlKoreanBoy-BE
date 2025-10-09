package masil.backend.modules.member.controller;

import lombok.RequiredArgsConstructor;
import masil.backend.global.security.annotation.LoginMember;
import masil.backend.global.security.dto.MemberDetails;
import masil.backend.modules.member.dto.response.MyInfoResponse;
import masil.backend.modules.member.service.MemberHighService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberHighService memberHighService;

    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getMyInfo(
            @LoginMember MemberDetails memberDetails
    ) {
        MyInfoResponse response = memberHighService.getMyInfo(memberDetails.memberId());
        return ResponseEntity.ok(response);
    }
}
