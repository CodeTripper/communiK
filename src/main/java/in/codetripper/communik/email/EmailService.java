package in.codetripper.communik.email;

import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<NotificationStatusResponse> send(EmailDto emailDto);

    Flux<NotificationMessageRepoDto> getAllEmails();

}
