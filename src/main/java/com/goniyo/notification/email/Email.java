package com.goniyo.notification.email;

import com.goniyo.notification.notification.NotificationMessage;

public class Email extends NotificationMessage {
    private String subject;
    private String attachment;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
