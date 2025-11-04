package masil.backend.modules.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.dto.response.MailboxLayerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailValidationService {

    private final RestClient restClient;

    @Value("${mailboxlayer.api-key}")
    private String apiKey;

    public boolean isEmailValid(final String email) {
        try {
            MailboxLayerResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("apilayer.net")
                            .path("/api/check")
                            .queryParam("access_key", apiKey)
                            .queryParam("email", email)
                            .queryParam("smtp", 0)
                            .queryParam("format", 1)
                            .build())
                    .retrieve()
                    .body(MailboxLayerResponse.class);

            if (response == null) {
                log.warn("Mailboxlayer API 응답이 null입니다. 이메일: {}", email);
                return true;
            }

            log.debug("Mailboxlayer API 응답: {}", response);

            boolean isValid = response.isValid();

            if (!isValid) {
                log.info("이메일 검증 실패: {} - 형식: {}, 일회용: {}, 점수: {}",
                        email,
                        response.getFormatValid(),
                        response.getDisposable(),
                        response.getScore()
                );
            } else {
                log.info("이메일 검증 성공: {} - 점수: {}", email, response.getScore());
            }

            return isValid;

        } catch (Exception e) {
            log.error("Mailboxlayer API 호출 실패. 이메일: {}", email, e);
            return true;
        }
    }
}
