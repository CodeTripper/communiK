package in.codetripper.communik.template;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationTemplateRepository extends ReactiveMongoRepository<NotificationTemplateRepoDto, String> {
}
