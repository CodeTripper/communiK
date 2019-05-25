package in.codetripper.notification.provider;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProviderMongoRepository extends ReactiveMongoRepository<ProviderDto, String> {
}
