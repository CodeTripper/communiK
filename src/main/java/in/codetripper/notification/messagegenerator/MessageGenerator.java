package in.codetripper.notification.messagegenerator;


import reactor.core.publisher.Mono;

public interface MessageGenerator<T> {
    Mono<String> generateMessage(String template, T notificationMessage) throws MessageGenerationException;

    String generateBlockingMessage(String template, T notificationMessage) throws MessageGenerationException;
}
