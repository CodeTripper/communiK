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
package in.codetripper.communik.config;

import static in.codetripper.communik.Constants.CACHE_DEFAULT;
import static in.codetripper.communik.Constants.CACHE_TEMPLATE;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CommunikCacheConfig extends CachingConfigurerSupport {

    @Override
    @Bean
    public CacheManager cacheManager() {
        CaffeineCache notificationCache = buildCache(CACHE_TEMPLATE,
                60); // TODO cache to be in config
        CaffeineCache defaultCache = buildCache(CACHE_DEFAULT, 300);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(notificationCache, defaultCache));
        return manager;
    }

    /*
     * Can be used like this @Cacheable(cacheNames = CACHE_TEMPLATE, cacheManager =
     * "distributedCacheManager")
     */
    @Bean
    public CacheManager distributedCacheManager() {
        CaffeineCache notificationCache = buildCache(CACHE_TEMPLATE, 10);
        CaffeineCache defaultCache = buildCache(CACHE_DEFAULT, 300);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(notificationCache, defaultCache));
        return manager;
    }

    private CaffeineCache buildCache(String name, int secondsToExpire) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(secondsToExpire, TimeUnit.SECONDS).maximumSize(100).build());
    }
}
