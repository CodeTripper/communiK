package com.goniyo.notification.webhooks;

import com.goniyo.notification.notification.NotificationMessage;

import java.util.List;

public class WebhookClient {
    private String clientId;
    private String clientName;
    private List<NotificationMessage.Status> interests;
    private List<NotificationMessage.Type> notificationTypes;
    private String webhook;
    private String active;

    // TODO equals and hashcode?
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<NotificationMessage.Status> getInterests() {
        return interests;
    }

    public void setInterests(List<NotificationMessage.Status> interests) {
        this.interests = interests;
    }

    public List<NotificationMessage.Type> getNotificationTypes() {
        return notificationTypes;
    }

    public void setNotificationTypes(List<NotificationMessage.Type> notificationTypes) {
        this.notificationTypes = notificationTypes;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String isActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
