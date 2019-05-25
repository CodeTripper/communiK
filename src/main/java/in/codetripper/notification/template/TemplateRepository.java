package in.codetripper.notification.template;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TemplateRepository extends ReactiveMongoRepository<TemplateRepoDto, String> {
}
