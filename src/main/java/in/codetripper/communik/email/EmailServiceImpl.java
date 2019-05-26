package in.codetripper.communik.email;

import in.codetripper.communik.messagegenerator.MessageGenerationException;
import in.codetripper.communik.messagegenerator.MessageGenerator;
import in.codetripper.communik.notification.*;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
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
    private Type notificationType = Type.EMAIL;
    @Autowired
    private Notification<Email> notificationHandler;
    @Autowired
    private MessageGenerator messageGenerator;
    @Autowired
    private EmailNotifier<Email> notifier;
    @Autowired
    private NotificationPersistence<Email> notificationPersistence; // TODO here?
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private NotificationTemplateService templateService;
    // TODO add validation here
    public Mono<NotificationStatusResponse> send(EmailDto emailDto) {
        return templateService.get(emailDto.getTemplateId()).
                single().
                map(this::validateTemplate).
                onErrorMap(original -> new InvalidRequestException("Invalid Request", original)).
                map(t -> generateMessage(t, emailDto)).
                map(message -> getEmail(emailDto, message)).
                flatMap(notificationHandler::sendNotification).
                doOnError(err -> log.error("Error while sending Email", err));

    }

    @Override
    public Flux<NotificationMessageRepoDto> getAllEmails() {
        return notificationPersistence.getAll();
    }

    private Email getEmail(EmailDto emailDto, String body) {
        Email email = emailMapper.emailDtoToEmail(emailDto);
        email.setSubject(emailDto.getSubject());
        email.setBodyTobeSent(body);
        NotificationMessage.Notifiers notifiers = new NotificationMessage.Notifiers();
        // TODO get provider here based on
        notifiers.setPrimary(notifier);
        //notifiers.setBackup();
        email.setNotifiers(notifiers);
        NotificationMessage.Meta meta = new NotificationMessage.Meta();
        // get IP
        meta.setSenderIp(null);
        meta.setType(notificationType);
        // meta.setCategory(template.getCategory());
        //meta.setLob(template.getLob());
        meta.setCreated(LocalDateTime.now());
        email.setMeta(meta);
        return email;
    }

    private NotificationTemplate validateTemplate(NotificationTemplate template) {
        log.debug("validating {}", template);
        if (template == null) {
            throw new InvalidRequestException("Template not found");
        }
        if (!template.getType().equals(notificationType)) {
            throw new InvalidRequestException("Notification type and Template mismatch");
        }
        return template;
    }


    private String generateMessage(NotificationTemplate template, EmailDto emailDto) {
        String message = "";
        try {
            if (emailDto.getTemplateId().isEmpty()) {
                message = emailDto.getBody().getMessage();
            } else {
                message = messageGenerator.generateBlockingMessage(template.getBody(), emailDto);
            }
        } catch (MessageGenerationException e) {
            e.printStackTrace();
        }
        log.debug("generated message {}", message);
        return message;

    }

}
