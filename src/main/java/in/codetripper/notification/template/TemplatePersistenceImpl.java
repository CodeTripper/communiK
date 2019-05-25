package in.codetripper.notification.template;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TemplatePersistenceImpl implements TemplatePersistence {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateMapper templateMapper;

    @Override
    @HystrixCommand()
    public Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate) {
        log.debug("notificationTemplate TemplateServiceImpl:{}", notificationTemplate);
        TemplateRepoDto templateRepoDto = templateMapper.templateToTemplateRepoDto(notificationTemplate);
        return this.templateRepository
                .insert(templateRepoDto).map(
                        te ->
                                templateMapper.templateRepoDtotoTemplate(te))
                .doOnSuccess(templateDto -> {
                });
    }

    @Override
    @HystrixCommand()
    public Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate) {
        log.debug("notificationTemplate TemplateServiceImpl:{}", notificationTemplate);
        TemplateRepoDto templateRepoDto = templateMapper.templateToTemplateRepoDto(notificationTemplate);
        return this.templateRepository
                .save(templateRepoDto).map(
                        te -> templateMapper.templateRepoDtotoTemplate(te))
                .doOnSuccess(templateDto -> {
                });
    }

    @Override
    @HystrixCommand()
    public Flux<NotificationTemplate> getAll() {
        return this.templateRepository
                .findAll().map(te -> templateMapper.templateRepoDtotoTemplate(te));
    }

    @Override
    @HystrixCommand()
    public Mono<NotificationTemplate> get(String id) {
        return this.templateRepository
                .findById(id).map(te -> templateMapper.templateRepoDtotoTemplate(te));
    }

    @Override
    @HystrixCommand()
    public Mono<Void> delete(String id) {
        return this.templateRepository
                .deleteById(id);
    }

}
