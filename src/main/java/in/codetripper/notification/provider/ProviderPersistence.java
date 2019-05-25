package in.codetripper.notification.provider;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderPersistence {

    Mono<Provider> getProvider(String id);

    Flux<Provider> getAll();
}
