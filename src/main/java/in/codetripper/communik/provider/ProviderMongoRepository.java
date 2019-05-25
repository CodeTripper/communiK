package in.codetripper.communik.provider;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProviderMongoRepository extends ReactiveMongoRepository<ProviderDto, String> {
}
