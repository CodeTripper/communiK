package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.Status;
import com.goniyo.notification.notification.Type;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class NotificationMessageDto {

    private @Id
    String id;
    private LocalDateTime created;
    private Type type;
    private String message;
    private String to;
    private String senderIp;
    private Status status;
    private LocalDateTime updated;
}
