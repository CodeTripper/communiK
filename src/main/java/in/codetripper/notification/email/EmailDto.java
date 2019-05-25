package in.codetripper.notification.email;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import in.codetripper.notification.notification.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ToString
public class EmailDto implements Serializable {
    @Email(message = "Email should be valid")
    private String to;
    private String subject;
    private Type type = Type.EMAIL;
    private Container body;
    private Container attachment;
    private String templateId;
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
