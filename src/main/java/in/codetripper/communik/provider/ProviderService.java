package in.codetripper.communik.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProviderService {
    private final ProviderPersistence providerPersistence;
    private Map<String, Provider> providerMap;

    @PostConstruct
    private void init() {
        providerMap = new HashMap<>();
        log.info("ProviderService initialization logic ...");
        getAllProviders().filter(Provider::isActive).doOnNext(item -> providerMap.put(item.getId(), item)).doOnError(error -> log.error("Error while initializing providers {0}", error)).subscribe();
    }

    public Provider getProvider(String id) {
        log.debug("searching providerid {} from providerMap {}", id, providerMap);
        return providerMap.get(id);
    }

    public Flux<Provider> getAllProviders() {
        log.info("Getting all providers...");
        return providerPersistence.getAll();
    }
}
