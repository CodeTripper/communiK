package com.goniyo.notification.template;

public class NotificationEvent<T> {
    private T what;
    protected boolean success;

    public NotificationEvent(T what, boolean success) {
        this.what = what;
        this.success = success;
    }

}
