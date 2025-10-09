package masil.backend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import masil.backend.global.security.dto.SecurityErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {
        final SecurityErrorResponse err = new SecurityErrorResponse("권한이 없습니다.");

        String json = objectMapper.writeValueAsString(err);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(json);
    }
}
