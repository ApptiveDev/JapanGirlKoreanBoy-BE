package masil.backend.modules.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import masil.backend.modules.member.dto.request.EmailVerificationCodeRequest;
import masil.backend.modules.member.dto.request.SignInRequest;
import masil.backend.modules.member.dto.request.SignUpRequest;
import masil.backend.modules.member.dto.request.VerifyEmailCodeRequest;
import masil.backend.modules.member.dto.response.SignInResponse;
import masil.backend.modules.member.service.MemberEmailVerificationHighService;
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
    public final MemberEmailVerificationHighService memberEmailVerificationHighService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        memberHighService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody final SignInRequest signInRequest) {
        final SignInResponse response = memberHighService.signIn(signInRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/email-code/send")
    public ResponseEntity<Void> sendVerificationCode(
            @Valid @RequestBody final EmailVerificationCodeRequest request
    ) {
        memberEmailVerificationHighService.sendVerificationCode(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email-code/verify")
    public ResponseEntity<Void> verifyEmailCode(
            @Valid @RequestBody final VerifyEmailCodeRequest request
    ) {
        memberEmailVerificationHighService.verifyEmailCode(request.email(), request.code());
        return ResponseEntity.ok().build();
    }
}
