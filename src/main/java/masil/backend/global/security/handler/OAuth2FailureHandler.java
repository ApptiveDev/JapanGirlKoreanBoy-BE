package masil.backend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 로그인 실패 시 처리하는 핸들러
 * 
 * OAuth2 로그인이 실패했을 때 적절한 에러 응답을 반환합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
        
        log.error("OAuth2 로그인 실패: {}", exception.getMessage(), exception);
        
        // 에러 응답 생성
        OAuth2ErrorResponse errorResponse = new OAuth2ErrorResponse(
                "OAuth2 로그인에 실패했습니다.",
                exception.getMessage()
        );
        
        // 응답 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        // JSON 응답 반환
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * OAuth2 에러 응답 DTO
     */
    public record OAuth2ErrorResponse(
            String message,
            String details
    ) {}
}