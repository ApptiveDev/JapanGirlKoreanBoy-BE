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
                            .queryParam("smtp", 1)
                            .queryParam("format", 1)
                            .build())
                    .retrieve()
                    .body(MailboxLayerResponse.class);

            if (response == null) {
                log.warn("Mailboxlayer API 응답이 null입니다. 이메일: {}", email);
                return true;
            }

            boolean isValid = response.isValid();

            if (!isValid) {
                log.info("이메일 검증 실패: {} - 형식: {}, MX레코드: {}, SMTP: {}, 일회용: {}",
                        email,
                        response.getFormatValid(),
                        response.getMxFound(),
                        response.getSmtpCheck(),
                        response.getDisposable()
                );
            } else {
                log.info("이메일 검증 성공: {}", email);
            }

            return isValid;

        } catch (Exception e) {
            log.error("Mailboxlayer API 호출 실패. 이메일: {}", email, e);
            return true;
        }
    }
}
