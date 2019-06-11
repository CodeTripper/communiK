package in.codetripper.communik.messagegenerator;

import reactor.core.publisher.Mono;

public interface AttachmentHandler<T, R> {

  Mono<R> get(String source, T data, String locale);
}
