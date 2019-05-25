package in.codetripper.notification.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoNotificationRepository extends ReactiveMongoRepository<NotificationMessageDto, String> {
}
