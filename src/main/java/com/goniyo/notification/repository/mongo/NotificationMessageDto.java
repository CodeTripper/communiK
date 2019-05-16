package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.NotificationMessage;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class NotificationMessageDto {

    public enum Type {EMAIL, SMS}

    private @Id
    String id;
    private LocalDateTime created;
    private NotificationMessage.Type type;
    private String message;
    private String to;
    private String senderIp;
    private NotificationMessage.Status status;
    private LocalDateTime updated;
}
