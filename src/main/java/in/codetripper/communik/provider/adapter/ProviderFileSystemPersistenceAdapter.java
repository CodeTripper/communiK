package in.codetripper.communik.provider.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "notification.provider.location", havingValue = "filesystem")
public class ProviderFileSystemPersistenceAdapter implements ProviderPersistence {
    private final ResourceLoader resourceLoader;


    @Value("${notification.provider.location.filesystem:classpath:providers}")
    private String providersPath;

    @Override
    public Mono<Provider> getProvider(String id) {
        Resource resource = resourceLoader.getResource(providersPath + "/" + id + ".json");
        log.debug("template resource {}", id);
        Provider provider = apply(resource);
        log.debug("returning template {}", provider);
        return Mono.justOrEmpty(provider);

    }

    @Override
    public Flux<Provider> getAll() throws IOException {
        return Flux.fromIterable(Arrays.asList(loadResources(providersPath + "/*.json"))).map(ProviderFileSystemPersistenceAdapter::apply);

    }

    private static Provider apply(Resource p) {
        Provider provider = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            provider = objectMapper.readValue(p.getFile(), Provider.class);
        } catch (IOException e) {
            log.error("Error while mapping all provider jsons", e);
        }
        return provider;
    }

    Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }
}
