package com.goniyo.notification.notification;

import org.springframework.context.ApplicationEvent;

// TODO remove applicationEvent
public class NotificationSentEvent<T> extends ApplicationEvent {
    private T what;
    public boolean success;

    public NotificationSentEvent(T source, boolean success) {
        super(source);
        this.what = source;
        this.success = success;
    }

}
