package in.codetripper.communik.email;

import in.codetripper.communik.notification.NotificationService;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.notification.Type;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// create base service and move common methods up
public interface EmailService extends NotificationService {
    Mono<NotificationStatusResponse> send(EmailDto emailDto);

    Flux<NotificationMessageRepoDto> getAllEmails();

    default Type getType() {
        return Type.EMAIL;
    }

}
