package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.repository.NotificationStorage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MongoDbStorageHandler implements NotificationStorage {
    @Override
    public NotificationStorageResponse store(NotificationMessage notificationMessage) {
        // TODO write to mongo
        System.out.println("stored");
        return new NotificationStorageResponse();
    }

    @PostConstruct
    public void init() {
        System.out.println("repo from @service");
    }

    @Override
    public NotificationStorageResponse update(NotificationMessage notificationMessage) {
        System.out.println("updated");
        return new NotificationStorageResponse();
    }

    @Override
    public NotificationStorageResponse status(String id) {
        return null;
    }
}
