package com.goniyo.notification.email;

import com.goniyo.notification.notification.NotificationMessage;
import org.immutables.value.Value;

@Value.Immutable
public abstract class Email extends NotificationMessage {
    public abstract String to();
}
