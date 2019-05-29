package in.codetripper.communik.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationStatusResponse {
    private boolean status;
    private String clientRequestId;
    private String requestId;
    private String providerResponseId;
    private String providerResponseMessage;
    private String responseMessage;
    private LocalDateTime responseReceivedAt;
}
