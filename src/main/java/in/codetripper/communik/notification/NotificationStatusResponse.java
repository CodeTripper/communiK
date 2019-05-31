package in.codetripper.communik.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationStatusResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String traceId;
    private String responseId;
    private String providerResponseId;
    private String providerResponseMessage;


}
