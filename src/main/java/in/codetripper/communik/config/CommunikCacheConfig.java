package in.codetripper.communik.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static in.codetripper.communik.Constants.CACHE_DEFAULT;
import static in.codetripper.communik.Constants.CACHE_TEMPLATE;

@Configuration
@EnableCaching
public class CommunikCacheConfig extends CachingConfigurerSupport {
    @Override
    @Bean
    public CacheManager cacheManager() {
        CaffeineCache notificationCache = buildCache(CACHE_TEMPLATE, 60);
        CaffeineCache defaultCache = buildCache(CACHE_DEFAULT, 300);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(notificationCache, defaultCache));
        return manager;
    }

    /*
        Can be used like this @Cacheable(cacheNames = CACHE_TEMPLATE, cacheManager = "distributedCacheManager")
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
                .expireAfterWrite(secondsToExpire, TimeUnit.SECONDS)
                .maximumSize(100)
                .build());
    }
}

