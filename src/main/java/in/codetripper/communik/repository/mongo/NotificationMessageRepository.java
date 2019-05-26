package in.codetripper.communik.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationMessageRepository extends ReactiveMongoRepository<NotificationMessageRepoDto, String> {
}
