package masil.backend.modules.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import masil.backend.modules.member.dto.request.SignInRequest;
import masil.backend.modules.member.dto.request.SignUpRequest;
import masil.backend.modules.member.dto.response.SignInResponse;
import masil.backend.modules.member.service.MemberHighService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberAuthController {

    public final MemberHighService memberHighService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody final SignUpRequest signUpReqeust) {
        memberHighService.signUp(signUpReqeust);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody final SignInRequest signUpReqeust) {
        final SignInResponse response = memberHighService.signIn(signUpReqeust);
        return ResponseEntity.ok(response);
    }
}
