package in.codetripper.notification.sms;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import in.codetripper.notification.notification.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ToString
public class SmsDto implements Serializable {
    private String to;
    private Type type = Type.SMS;
    private Container body;
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
