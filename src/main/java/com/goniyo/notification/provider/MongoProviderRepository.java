package com.goniyo.notification.provider;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoProviderRepository extends ReactiveMongoRepository<ProviderDto, String> {
}
