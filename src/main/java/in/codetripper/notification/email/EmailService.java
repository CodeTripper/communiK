package in.codetripper.notification.email;

import in.codetripper.notification.notification.NotificationStatusResponse;
import in.codetripper.notification.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<NotificationStatusResponse> send(EmailDto emailDto);
    Flux<NotificationMessageDto> getAllEmails();

}
