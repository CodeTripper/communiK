package in.codetripper.communik.notification;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class Notification<T extends NotificationMessage> {
    @Autowired
    private NotificationPersistence<T> notificationPersistence;
    // Store // DONE
    // Add Timeout for DB // DONE
    // Transform message // DONE
    // Call primary provider // DONE
    // Add Timeout for primary provider // DONE
    // Update DB or Error Or Failure on a separate Threadpool // TODO
    // Call Fallback Notifier // TODO
    // Update DB or Error Or Failure on a separate Threadpool
    // If Failed send a generic Error


    public Mono<NotificationStatusResponse> sendNotification(@NonNull T notificationMessage) {
        log.info("About to persist notification for {}", notificationMessage.getTo());
        log.debug("About to persist notification for {}", notificationMessage);
        // TODO how not to save reruns? Use existing id to bypass
        // Schedulers.parallel()
        notificationMessage.setStatus(Status.NOTIFICATION_NEW);
        return notificationPersistence.store(notificationMessage)
                //.timeout(Duration.ofMillis(1800))
                .map(status -> {
                    notificationMessage.setStatus(Status.NOTIFICATION_STORED);
                    notificationMessage.setId(status.getId());
                    return notificationMessage;
                })
                .flatMap(message -> message.getNotifiers().getPrimary().send(message))// NullPOinterHere if no provider
                // to be put down in the chain
                .flatMap(status -> update(notificationMessage)) // publish on use an executor service .subscribeOn(Schedulers.elastic())
                .flatMap(status -> getStatus((NotificationStorageResponse) status))
                // .timeout(Duration.ofMillis(4800))
                // create a new request Id
                .doOnError(err -> log.error("Error while sending Notification", err))
                .onErrorResume(message -> notificationMessage.getNotifiers().getPrimary().send(notificationMessage))
                .onErrorReturn(getFailure(notificationMessage));

    }

    private Mono<NotificationStatusResponse> getStatus(NotificationStorageResponse status) {
        NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
        NotificationStorageResponse updateStatus = status;
        notificationStatusResponse.setRequestId(updateStatus.getId());
        notificationStatusResponse.setStatus(updateStatus.isStatus());
        notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
        return Mono.just(notificationStatusResponse);
    }

    private Mono<NotificationMessage> update(@NonNull T notificationMessage) {
        notificationMessage.setStatus(Status.NOTIFICATION_SUCCESS);
        notificationMessage.setAttempts(1);
        NotificationMessage.Action action = new NotificationMessage.Action();
        action.setEnded(LocalDateTime.now());
        action.setStatus(false);
        action.setNotifier(notificationMessage.getNotifiers().getPrimary());
        notificationMessage.setActions(List.of(action));
        return notificationPersistence.update(notificationMessage);
    }

    private Mono<NotificationStatusResponse> getFailure(@NonNull T notificationMessage) {
        NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
        notificationStatusResponse.setRequestId(notificationMessage.getId());
        notificationStatusResponse.setStatus(false);
        notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
        return Mono.just(notificationStatusResponse);
    }

}
