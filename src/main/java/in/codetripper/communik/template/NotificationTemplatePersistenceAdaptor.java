package in.codetripper.communik.template;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import in.codetripper.communik.repository.mongo.NotificationTemplateRepoDto;
import in.codetripper.communik.repository.mongo.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationTemplatePersistenceAdaptor implements NotificationTemplatePersistence {
    private final NotificationTemplateRepository templateRepository;
    private final NotificationTemplateMapper templateMapper;

    @Override
    @HystrixCommand()
    public Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate) {
        log.debug("notificationTemplate NotificationTemplateServiceImpl:{}", notificationTemplate);
        NotificationTemplateRepoDto templateRepoDto = templateMapper.templateToTemplateRepoDto(notificationTemplate);
        return this.templateRepository
                .insert(templateRepoDto).map(
                        templateMapper::templateRepoDtotoTemplate)
                .doOnSuccess(templateDto -> {
                });
    }

    @Override
    @HystrixCommand()
    public Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate) {
        log.debug("notificationTemplate NotificationTemplateServiceImpl:{}", notificationTemplate);
        NotificationTemplateRepoDto templateRepoDto = templateMapper.templateToTemplateRepoDto(notificationTemplate);
        return this.templateRepository
                .save(templateRepoDto).map(
                        templateMapper::templateRepoDtotoTemplate)
                .doOnSuccess(templateDto -> {
                });
    }

    @Override
    @HystrixCommand()
    public Flux<NotificationTemplate> getAll() {
        return this.templateRepository
                .findAll().map(templateMapper::templateRepoDtotoTemplate);
    }

    @Override
    @HystrixCommand()
    public Mono<NotificationTemplate> get(String id) {
        return this.templateRepository
                .findById(id).map(templateMapper::templateRepoDtotoTemplate);
    }

    @Override
    @HystrixCommand()
    public Mono<Void> delete(String id) {
        return this.templateRepository
                .deleteById(id);
    }

}
