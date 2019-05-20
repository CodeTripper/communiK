package com.goniyo.notification.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationStatusResponse {
    private String clientRequestId;
    private boolean status;
    private String requestId;
    private String providerResponseId;
    private String response;
    private LocalDateTime responseReceivedAt;
}
