package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationStorage;
import com.goniyo.notification.webhooks.WehhookHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationHandler<T extends NotificationMessage> {
    private static final Logger logger = LoggerFactory.getLogger(NotificationHandler.class);
    @Autowired
    private NotificationStorage notificationStorage;

    private List<Notifier<T>> backupNotifiers;

    public void setBackupNotifierHandlers(List<Notifier<T>> backupNotifiers) {
        this.backupNotifiers = backupNotifiers;

    }


    public String sendNotification(Notifier<T> notifier, T notificationMessage) {
        logger.info("sending notification" + notificationMessage);
        // TODO register themselves
        notificationMessage.addObserver(new WehhookHandler());
        notificationMessage.addObserver(notificationStorage);
        String returnValue = null;
        try {
            returnValue = notifier.send(notificationMessage);
            notificationMessage.setStatus("SUCCESS");
        } catch (NotificationFailedException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    private void retryWithBackup(T notificationMessage) {
        // FIXME exit after first success
        backupNotifiers.forEach(notifier -> {
            try {
                notifier.send(notificationMessage);
            } catch (NotificationFailedException e) {
                e.printStackTrace();
            }
        });

    }
    public String getNotificationStatus(String id) {
        NotificationStorageResponse notificationStorageResponse = notificationStorage.status(id);
        return "SUCCESS";
    }

   /* private VehicleDao fallbackNotifiers() {
        foreach(VehicleDao vehicleDao : vehicleDaos) {
            if(vehicleDao.isResponsibleFor(vehicle) {
                return vehicleDao;
            }
        }

        throw new UnsupportedOperationException("unsupported vehicleType");
    }*/

    /*public void doSomething(String input) {
        backupNotifiers.stream().filter(c -> c.getName().contains(input)).findFirst().ifPresent(c -> {
            System.out.println(input);
        });
    }*/
}
