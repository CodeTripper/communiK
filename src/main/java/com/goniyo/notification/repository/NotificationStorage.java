package com.goniyo.notification.repository;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;

import java.util.Observer;

public interface NotificationStorage extends Observer {
    NotificationStorageResponse store(NotificationMessage notificationMessage);

    NotificationStorageResponse update(NotificationMessage notificationMessage);

    NotificationStorageResponse status(String id);
}
