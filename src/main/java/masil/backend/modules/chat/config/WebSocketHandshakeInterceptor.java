package masil.backend.modules.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.global.security.dto.MemberDetails;
import masil.backend.global.security.provider.JwtProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    
    private final JwtProvider jwtProvider;
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        // 쿼리 파라미터에서 토큰 추출
        URI uri = request.getURI();
        String query = uri.getQuery();
        String token = null;
        
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    token = param.substring("token=".length());
                    break;
                }
            }
        }
        
        // 헤더에서 토큰 추출 (쿼리 파라미터에 없을 경우)
        if (token == null) {
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }
        
        // 토큰 검증 및 인증 정보 추출
        if (token != null && jwtProvider.validateToken(token)) {
            try {
                Authentication authentication = jwtProvider.getAuthentication(token);
                if (authentication != null && authentication.getPrincipal() instanceof MemberDetails) {
                    MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
                    attributes.put("memberId", memberDetails.memberId());
                    attributes.put("memberDetails", memberDetails);
                    log.info("WebSocket 핸드셰이크 성공: memberId={}", memberDetails.memberId());
                    return true;
                }
            } catch (Exception e) {
                log.error("WebSocket 핸드셰이크 중 인증 실패: {}", e.getMessage(), e);
            }
        }
        
        log.warn("WebSocket 핸드셰이크 실패: 유효하지 않은 토큰 또는 토큰 없음");
        return false;
    }
    
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket 핸드셰이크 후 오류 발생: {}", exception.getMessage(), exception);
        }
    }
}

