package in.codetripper.communik.provider.adapter;

import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderMapper;
import in.codetripper.communik.provider.ProviderPersistence;
import in.codetripper.communik.repository.mongo.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "notification.provider.location", havingValue = "mongo", matchIfMissing = true)
public class ProviderMongoPersistenceAdapter implements ProviderPersistence {
    private final ProviderRepository mongoNotifierRepository;
    private final ProviderMapper notifierMapper;

    @Override
    public Mono<Provider> getProvider(String id) {
        log.info("Getting provider details for provider {}", id);
        return mongoNotifierRepository.findById(id).map(notifierMapper::mapDtoToNotifier).doOnSuccess(message -> log.debug("Got provider {0}", message)).doOnError(error -> log.error("Exception while retrieving provider from mongo {}", error));
    }

    @Override
    public Flux<Provider> getAll() {
        return mongoNotifierRepository.findAll().map(notifierMapper::mapDtoToNotifier);
    }
}
