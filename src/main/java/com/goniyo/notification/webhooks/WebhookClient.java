package com.goniyo.notification.webhooks;

import com.goniyo.notification.notification.Status;
import com.goniyo.notification.notification.Type;

import java.util.List;

public class WebhookClient {
    private String clientId;
    private String clientName;
    private List<Status> interests;
    private List<Type> notificationTypes;
    private String webhook;
    private String active;

}
