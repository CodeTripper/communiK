package com.goniyo.notification.repository;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;

public interface NotificationStorage {
    NotificationStorageResponse store(NotificationMessage notificationMessage);

    NotificationStorageResponse update(NotificationMessage notificationMessage);

    NotificationStorageResponse status(String id);
}
