package masil.backend.modules.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MailboxLayerResponse {

    @JsonProperty("did_you_mean")
    private String didYouMean;

    @JsonProperty("user")
    private String user;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("format_valid")
    private Boolean formatValid;

    @JsonProperty("mx_found")
    private Boolean mxFound;

    @JsonProperty("smtp_check")
    private Boolean smtpCheck;

    @JsonProperty("catch_all")
    private Boolean catchAll;

    @JsonProperty("role")
    private Boolean role;

    @JsonProperty("disposable")
    private Boolean disposable;

    @JsonProperty("free")
    private Boolean free;

    @JsonProperty("score")
    private Double score;

    public boolean isValid() {
        return Boolean.TRUE.equals(formatValid)
                && Boolean.TRUE.equals(mxFound)
                && Boolean.TRUE.equals(smtpCheck)
                && Boolean.FALSE.equals(disposable);
    }
}
