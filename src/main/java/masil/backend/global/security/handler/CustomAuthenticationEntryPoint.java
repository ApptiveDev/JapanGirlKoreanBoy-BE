package masil.backend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import masil.backend.global.security.dto.SecurityErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint { // 인증 실패 시 동작

    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {

        final SecurityErrorResponse err = new SecurityErrorResponse("권한이 없습니다.");
        final String json = objectMapper.writeValueAsString(err);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(json);
    }
}
