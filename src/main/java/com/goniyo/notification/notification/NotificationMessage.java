package com.goniyo.notification.notification;

import java.time.LocalDateTime;
import java.util.Observable;

public class NotificationMessage extends Observable {
    private String id;
    public String type;
    public LocalDateTime timestamp;
    public String message;
    public String to;
    public String senderIp;
    private String status;// TODO enum

    // todo add subdomain
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
