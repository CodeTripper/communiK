/*
 * Copyright 2019 CodeTripper
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package in.codetripper.communik.domain.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProviderService<T> {

  private final ProviderPersistence providerPersistence;
  private Map<String, Provider> providerMap;

  @PostConstruct
  private void init() {
    providerMap = new HashMap<>();
    log.info("ProviderService initialization providerMap on startup...");
    try {
      getAllProviders().filter(Provider::isActive)
          .doOnNext(item -> providerMap.put(item.getId(), item))
          .doOnError(error -> log.error("Error while initializing providers", error)).subscribe();
    } catch (IOException e) {
      log.error("Unable to initialize providers map", e);
    }
  }

  public Provider<T> getProvider(String id) {
    log.debug("searching providerid {} from providerMap {}", id, providerMap);
    return providerMap.get(id);
  }

  public Flux<Provider> getAllProviders() throws IOException {
    log.info("Getting all providers...");
    return providerPersistence.getAll();
  }
}
