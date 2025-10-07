package masil.backend.global.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import masil.backend.global.security.dto.MemberDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);
    private static final String NAME = "name";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(final String userPk, final String name) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(userPk)
                .claim(NAME, name)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(final String token) {
        final Claims claims = getClaim(token);
        final String memberId = claims.getSubject();
        final String name = claims.get(NAME, String.class);

        final MemberDetails memberDetails = new MemberDetails(
                Long.parseLong(memberId),
                name
        );

        return new UsernamePasswordAuthenticationToken(
                memberDetails,
                null,
                List.of()
        );
    }

    public String resolveToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 구조: {}", e.getMessage());
        } catch (SecurityException e) {
            log.error("서명 검증 실패: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("빈 JWT 토큰: {}", e.getMessage());
        }
        return false;
    }

    private Claims getClaim(final String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String extractSubject(final String token) {
        return getClaim(token).getSubject();
    }
}
