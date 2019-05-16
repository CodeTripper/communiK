package com.goniyo.notification.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.UUID;
// TODO change the below hack to Superbuilder when milestone 25 is released Idea plugin
// https://github.com/mplushnikov/lombok-intellij-plugin/milestone/31


@Getter
@ToString
@Slf4j
@NoArgsConstructor

public class NotificationMessage {
    public enum Status {NOTIFICATION_NEW, NOTIFICATION_STORED, NOTIFICATION_SENT, NOTIFICATION_FAILED, NOTIFICATION_RETRY_FAILED}

    public enum Type {EMAIL, SMS}

    private @Id
    String id;
    private LocalDateTime created;
    private Type type;
    private String message;
    private @NotNull String to;
    private String senderIp;
    private transient PropertyChangeSupport propertyChangeSupport;
    private Status status;
    private LocalDateTime updated;

    public NotificationMessage(Type type, String message, String to, String senderIp, Status status) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.message = message;
        this.to = to;
        this.senderIp = senderIp;
        this.status = status;
        this.created = LocalDateTime.now();
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public final void setStatus(Status status) {
        this.status = status;
        propertyChangeSupport.firePropertyChange("status", status, this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        log.debug("pcl added");
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public final Status getStatus() {
        return this.status;
    }


}
