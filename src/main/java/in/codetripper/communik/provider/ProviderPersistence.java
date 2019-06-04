package in.codetripper.communik.provider;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface ProviderPersistence {

    Mono<Provider> getProvider(String id);

    Flux<Provider> getAll() throws IOException;
}
