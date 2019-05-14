package com.goniyo.notification.email;

import com.goniyo.notification.notification.NotificationMessage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Email extends NotificationMessage {
    private String subject;
    private String attachment;

    @Builder
    public Email(Type type, String message, String to, String senderIp, Status status, String subject, String attachment) {
        super(type, message, to, senderIp, status);
        this.subject = subject;
        this.attachment = attachment;
    }
}
