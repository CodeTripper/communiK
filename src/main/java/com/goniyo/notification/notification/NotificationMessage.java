package com.goniyo.notification.notification;

import java.time.LocalDateTime;
import java.util.Observable;

public class NotificationMessage extends Observable {
    private String id;
    private Type type;
    public LocalDateTime timestamp;
    public String message;
    public String to;
    public String senderIp;
    private Status status;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Status {NOTIFICATION_NEW, NOTIFICATION_STORED, NOTIFICATION_SENT, NOTIFICATION_FAILED, NOTIFICATION_RETRY_FAILED}

    public enum Type {EMAIL, SMS}


    // todo add subdomain
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        setChanged();
        notifyObservers(status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
