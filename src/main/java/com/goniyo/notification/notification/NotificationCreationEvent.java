package com.goniyo.notification.notification;

import org.springframework.context.ApplicationEvent;

// TODO remove applicationEvent
public class NotificationCreationEvent<T> extends ApplicationEvent {
    private T what;
    public boolean success;

    public NotificationCreationEvent(T source, boolean success) {
        super(source);
        this.what = source;
        this.success = success;
    }

}
