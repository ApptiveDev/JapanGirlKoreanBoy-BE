package KBJG.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KBJG.backend.dto.AuthRequest;
import KBJG.backend.dto.AuthResponse;
import KBJG.backend.dto.RefreshTokenRequest;
import KBJG.backend.dto.RegisterRequest;
import KBJG.backend.entity.User;
import KBJG.backend.repository.UserRepository;
import KBJG.backend.util.JwtUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    /**
     * 사용자 회원가입
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다");
        }
        
        // 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .provider(User.AuthProvider.LOCAL)
                .emailVerified(false)
                .role(User.Role.USER)
                .build();
        
        user = userRepository.save(user);
        log.info("새로운 사용자 등록: {}", user.getEmail());
        
        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        
        return createAuthResponse(accessToken, refreshToken, user);
    }
    
    /**
     * 사용자 로그인
     */
    public AuthResponse login(AuthRequest request) {
        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        User user = (User) authentication.getPrincipal();
        log.info("사용자 로그인: {}", user.getEmail());
        
        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        
        return createAuthResponse(accessToken, refreshToken, user);
    }
    
    /**
     * 토큰 갱신
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        // 리프레시 토큰 유효성 검증
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다");
        }
        
        String email = jwtUtil.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        log.info("토큰 갱신: {}", user.getEmail());
        
        // 새로운 토큰 생성
        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        
        return createAuthResponse(newAccessToken, newRefreshToken, user);
    }
    
    /**
     * AuthResponse 생성
     */
    private AuthResponse createAuthResponse(String accessToken, String refreshToken, User user) {
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole().name())
                .build();
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getExpirationDateFromToken(accessToken).getTime() - System.currentTimeMillis())
                .user(userInfo)
                .build();
    }
}
