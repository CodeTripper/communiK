package in.codetripper.notification.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j

public class ProviderAdapter implements ProviderPersistence {
    @Autowired
    private ProviderMongoRepository mongoNotifierRepository;
    @Autowired
    private ProviderMapper notifierMapper;

    @Override
    public Mono<Provider> getProvider(String id) {
        log.info("Getting provider details for provider {}", id);
        return mongoNotifierRepository.findById(id).map(notifierDto -> notifierMapper.mapDtoToNotifier(notifierDto)).doOnSuccess(message -> log.debug("Got provider {}", message)).doOnError(error -> log.debug("Exception while retrieving provider from mongo {}", error));
    }

    @Override
    public Flux<Provider> getAll() {
        return mongoNotifierRepository.findAll().map(notifierDto -> notifierMapper.mapDtoToNotifier(notifierDto));
    }
}
