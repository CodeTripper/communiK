package in.codetripper.communik.email;

import in.codetripper.communik.messagegenerator.MessageGenerationException;
import in.codetripper.communik.messagegenerator.MessageGenerator;
import in.codetripper.communik.notification.*;
import in.codetripper.communik.repository.NotificationPersistence;
import in.codetripper.communik.repository.mongo.NotificationMessageDto;
import in.codetripper.communik.template.NotificationTemplate;
import in.codetripper.communik.template.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
class EmailServiceImpl implements EmailService {
    @Autowired
    private Notification<Email> notificationHandler;
    @Autowired
    private MessageGenerator messageGenerator;
    @Autowired
    private EmailNotifier<Email> emailNotifier;
    @Autowired
    private NotificationPersistence<Email> notificationPersistence; // TODO here?
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private NotificationTemplateService templateService;
    // TODO add validation here
    public Mono<NotificationStatusResponse> send(EmailDto emailDto) {
        log.debug("Preparing data for Email Notification {}", emailDto);
        NotificationTemplate template = getTemplate(emailDto.getTemplateId());
        validate(template);

        String message = null;
        try {
            message = messageGenerator.generateBlockingMessage(template.getBody(), emailDto);

        } catch (MessageGenerationException e) {
            e.printStackTrace();
        }

        Email email = emailMapper.emailDtoToEmail(emailDto);
        NotificationMessage.Notifiers notifiers = new NotificationMessage.Notifiers();
        notifiers.setPrimary(emailNotifier);
        //notifiers.setBackup(emailNotifier);
        email.setNotifiers(notifiers);
        NotificationMessage.Meta meta = new NotificationMessage.Meta();
        // get IP
        meta.setSenderIp(null);
        meta.setType(Type.EMAIL);
        meta.setCategory(template.getCategory());
        meta.setLob(template.getLob());
        meta.setCreated(LocalDateTime.now());
        email.setMeta(meta);
        log.debug("Prepared data for Email Notification {}", email);
        return notificationHandler.sendNotification(email);

    }

    private void validate(NotificationTemplate template) {
        if (!template.getType().toString().equalsIgnoreCase(Type.EMAIL.toString())) {
            throw new InvalidRequestException("Notification type and Template mismatch");
        }
    }

    private NotificationTemplate getTemplate(String id) {
        // TODO remove block
        NotificationTemplate template = templateService.get(id).block();
        return template;
    }

    @Override
    public Flux<NotificationMessageDto> getAllEmails() {
        return notificationPersistence.getAll();
    }


}
