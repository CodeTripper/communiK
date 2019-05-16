package com.goniyo.notification.email;

import com.goniyo.notification.notification.NotificationMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
@NoArgsConstructor
@Slf4j
public class Email extends NotificationMessage {
    private String subject;
    private String attachment;

    @Builder
    public Email(Type type, String message, String to, String senderIp, Status status, String subject, String attachment) {
        super(type, message, to, senderIp, status);
        log.debug("Email created {}", this.getId());
        this.subject = subject;
        this.attachment = attachment;
    }
}
