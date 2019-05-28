package in.codetripper.communik.notification;

import in.codetripper.communik.exceptions.NotificationSendFailedException;
import reactor.core.publisher.Mono;

public interface Notifier<T extends NotificationMessage> {
    Mono<NotificationStatusResponse> send(T notificationMessage) throws NotificationSendFailedException;

    boolean isDefault();

}
