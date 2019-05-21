package com.goniyo.notification.provider;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProviderMongoRepository extends ReactiveMongoRepository<ProviderDto, String> {
}
