package in.codetripper.notification.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ProviderService {
    @Autowired
    private ProviderPersistence providerPersistence;
    private Map<String, Provider> providerMap;

    @PostConstruct
    private void init() {
        providerMap = new HashMap<>();
        log.info("ProviderService initialization logic ...");
        getAllProviders().doOnNext(item -> providerMap.put(item.getId(), item)).doOnError(error -> log.error("Error while initializing providers {}", error)).subscribe();
    }

    public Provider getProvider(String id) {
        log.debug("searching providerid {} from providerMap {}", id, providerMap);
        return providerMap.get(id);
    }

    // TODO filter active
    public Flux<Provider> getAllProviders() {
        log.info("Getting all providers...");
        return providerPersistence.getAll();
    }
}
