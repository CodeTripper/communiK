package in.codetripper.notification.notification;

import reactor.core.publisher.Mono;

public interface Notifier<T extends NotificationMessage> {
    Mono<NotificationStatusResponse> send(T notificationMessage); // FIXME throws NotificationFailedException;

}
