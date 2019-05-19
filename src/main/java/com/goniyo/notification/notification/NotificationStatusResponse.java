package com.goniyo.notification.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationStatusResponse {
    private boolean status;
    private String responseId;
    private String requestId;
    private String clientRequestId;
    private LocalDateTime responseReceivedAt;
}
