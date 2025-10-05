package KBJG.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import KBJG.backend.dto.AuthRequest;
import KBJG.backend.dto.AuthResponse;
import KBJG.backend.dto.RefreshTokenRequest;
import KBJG.backend.dto.RegisterRequest;
import KBJG.backend.service.UserService;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "인증 관리", description = "사용자 인증 관련 API")
public class AuthController {
    
    private final UserService userService;
    
    @Operation(
        summary = "회원가입",
        description = "이메일과 비밀번호를 사용하여 새 사용자를 등록합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원가입 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "tokenType": "Bearer",
                      "expiresIn": 900000,
                      "user": {
                        "id": 1,
                        "email": "user@example.com",
                        "name": "홍길동",
                        "profileImageUrl": null,
                        "role": "USER"
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "회원가입 정보", required = true)
            @Valid @RequestBody RegisterRequest request) {
        log.info("회원가입 요청: {}", request.getEmail());
        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "로그인",
        description = "이메일과 비밀번호를 사용하여 로그인합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "로그인 정보", required = true)
            @Valid @RequestBody AuthRequest request) {
        log.info("로그인 요청: {}", request.getEmail());
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "토큰 갱신",
        description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "토큰 갱신 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @Parameter(description = "Refresh Token 정보", required = true)
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("토큰 갱신 요청");
        AuthResponse response = userService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Google OAuth2 로그인",
        description = "Google 계정을 사용하여 소셜 로그인을 시작합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Google OAuth2 인증 페이지로 리다이렉트")
    })
    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        log.info("Google OAuth2 로그인 시작");
        response.sendRedirect("/api/oauth2/authorization/google");
    }
    
    @Operation(
        summary = "OAuth2 로그인 성공 처리",
        description = "OAuth2 로그인 성공 후 토큰을 반환합니다. (내부용)"
    )
    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauth2Success(
            @Parameter(description = "Access Token") @RequestParam String access_token,
            @Parameter(description = "Refresh Token") @RequestParam String refresh_token,
            @Parameter(description = "사용자 이메일") @RequestParam String email) {
        
        log.info("OAuth2 로그인 성공: {}", email);
        
        AuthResponse response = AuthResponse.builder()
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "현재 사용자 정보 조회",
        description = "JWT 토큰을 통해 현재 로그인한 사용자의 정보를 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "사용자 정보 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.UserInfo.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserInfo> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            log.info("현재 사용자 조회: {}", email);
            
            // 사용자 정보 조회 로직 추가 가능
            // 현재는 간단한 응답만 반환
            AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                    .email(email)
                    .build();
            
            return ResponseEntity.ok(userInfo);
        }
        
        return ResponseEntity.badRequest().build();
    }
    
    @Operation(
        summary = "로그아웃",
        description = "사용자 로그아웃을 처리합니다. (클라이언트에서 토큰 삭제 필요)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        log.info("로그아웃 요청");
        // JWT는 stateless하므로 서버에서는 별도 처리 불필요
        // 클라이언트에서 토큰 삭제하면 됨
        return ResponseEntity.ok("로그아웃되었습니다");
    }
}
