package masil.backend.modules.chat.config;

import lombok.RequiredArgsConstructor;
import masil.backend.modules.chat.handler.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .addInterceptors(webSocketHandshakeInterceptor) // JWT 인증 인터셉터 추가
                .setAllowedOrigins("*") // 개발 환경용, 프로덕션에서는 특정 도메인으로 제한
                .withSockJS(); // SockJS 지원 (폴백 옵션)
    }
}

