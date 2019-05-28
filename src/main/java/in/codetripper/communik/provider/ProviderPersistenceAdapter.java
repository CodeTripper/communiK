package in.codetripper.communik.provider;

import in.codetripper.communik.repository.mongo.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProviderPersistenceAdapter implements ProviderPersistence {
    private final ProviderRepository mongoNotifierRepository;
    private final ProviderMapper notifierMapper;

    @Override
    public Mono<Provider> getProvider(String id) {
        log.info("Getting provider details for provider {}", id);
        return mongoNotifierRepository.findById(id).map(notifierDto -> notifierMapper.mapDtoToNotifier(notifierDto)).doOnSuccess(message -> log.debug("Got provider {0}", message)).doOnError(error -> log.error("Exception while retrieving provider from mongo {}", error));
    }

    @Override
    public Flux<Provider> getAll() {
        return mongoNotifierRepository.findAll().map(notifierDto -> notifierMapper.mapDtoToNotifier(notifierDto));
    }
}
