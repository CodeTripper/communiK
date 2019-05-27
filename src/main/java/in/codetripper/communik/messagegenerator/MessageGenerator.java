package in.codetripper.communik.messagegenerator;


import reactor.core.publisher.Mono;

public interface MessageGenerator<T> {
    Mono<String> generateMessage(String template, T notificationMessage);

    String generateBlockingMessage(String template, T notificationMessage);
}
