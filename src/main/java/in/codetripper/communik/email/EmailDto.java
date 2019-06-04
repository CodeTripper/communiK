package in.codetripper.communik.email;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import in.codetripper.communik.notification.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static in.codetripper.communik.exceptions.ExceptionConstants.VALIDATION_EMAIL_EMPTY_EMAIL;

@Data
@ToString
public class EmailDto implements Serializable {
    @NotEmpty(message = VALIDATION_EMAIL_EMPTY_EMAIL)
    //  @Email(message = VALIDATION_EMAIL_INVALID_EMAIL)
    private List<String> to;
    private String subject;
    private Type type = Type.EMAIL;
    private Container body;
    private Container attachment;
    private String templateId;
    private String providerName;
    @Data
    @NoArgsConstructor
    public static class Container {
        private String message;
        Map<String, Object> data = new LinkedHashMap<>();

        @JsonAnySetter
        void setDetail(String key, Object value) {
            data.put(key, value);

        }
    }
}
