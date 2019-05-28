package in.codetripper.communik.notification;

import in.codetripper.communik.exceptions.NotificationPersistenceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static in.codetripper.communik.Constants.DB_WRITE_TIMEOUT;
import static in.codetripper.communik.Constants.PROVIDER_TIMEOUT;

@Component
@Slf4j
@RequiredArgsConstructor
public class Notification<T extends NotificationMessage> {

    private final NotificationPersistence<T> notificationPersistence;
    // Update DB or Error Or Failure on a separate Threadpool // TODO
    // Call Fallback Notifier // TODO
    // Update DB or Error Or Failure on a separate Threadpool
    /*public Notification(NotificationPersistence<T> notificationPersistence){
        this.notificationPersistence=notificationPersistence;
    }*/

    public Mono<NotificationStatusResponse> sendNotification(@NonNull T notificationMessage) {
        log.info("About to persist notification for {}", notificationMessage.getTo());
        log.debug("About to persist notification for {}", notificationMessage);
        // TODO how not to save reruns? Use existing id to bypass
        // Schedulers.parallel()
        notificationMessage.setStatus(Status.NOTIFICATION_NEW);
        return notificationPersistence.store(notificationMessage)
                .timeout(Duration.ofMillis(DB_WRITE_TIMEOUT))
                .map(status -> {
                    notificationMessage.setStatus(Status.NOTIFICATION_STORED);
                    notificationMessage.setId(status.getId());
                    return notificationMessage;
                })
                .flatMap(message -> {
                    NotificationMessage.Notifiers<T> noti = notificationMessage.getNotifiers();
                    return noti.getPrimary().send(notificationMessage);
                })
                .timeout(Duration.ofMillis(PROVIDER_TIMEOUT))
                .flatMap(status -> update(notificationMessage))
                .timeout(Duration.ofMillis(DB_WRITE_TIMEOUT))
                .flatMap(this::createResponse)
                .doOnError(err -> log.error("Error while sending Notification", err))
                .onErrorResume(error ->
                {
                    log.info("primary provider failed to send notification {0}", error);
                    if (error instanceof NotificationPersistenceException) {
                        return Mono.error(error);
                    } else {
                        log.warn("retrying again as primary provider failed to send notification");
                        NotificationMessage.Notifiers<T> noti = notificationMessage.getNotifiers();
                        return noti.getPrimary().send(notificationMessage);
                    }

                })
                // .flatMap(status -> update(notificationMessage))
                .onErrorReturn(getFailure());

    }

    private Mono<NotificationStatusResponse> createResponse(NotificationStorageResponse status) {
        NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
        notificationStatusResponse.setRequestId(status.getId());
        notificationStatusResponse.setStatus(status.isStatus());
        notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
        return Mono.just(notificationStatusResponse);
    }

    private Mono<NotificationStorageResponse> update(@NonNull T notificationMessage) {
        notificationMessage.setStatus(Status.NOTIFICATION_SUCCESS);
        notificationMessage.setAttempts(1);
        NotificationMessage.Action<T> action = new NotificationMessage.Action<>();
        action.setEnded(LocalDateTime.now());
        action.setStatus(false);
        NotificationMessage.Notifiers<T> primary = notificationMessage.getNotifiers();
        Notifier<T> test = primary.getPrimary();
        action.setNotifier(test.getClass().getName());
        notificationMessage.setActions(List.of(action));
        return notificationPersistence.update(notificationMessage);
    }

    private NotificationStatusResponse getFailure() {
        NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
        //  notificationStatusResponse.setRequestId(notificationMessage.getId()); // FIXME
        notificationStatusResponse.setStatus(false);
        notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
        return notificationStatusResponse;
    }

}

