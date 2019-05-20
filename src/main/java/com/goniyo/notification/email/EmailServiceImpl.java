package com.goniyo.notification.email;

import com.goniyo.notification.messagegenerator.MessageGenerationException;
import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStatusResponse;
import com.goniyo.notification.notification.Type;
import com.goniyo.notification.repository.NotificationPersistence;
import com.goniyo.notification.repository.mongo.NotificationMessageDto;
import com.goniyo.notification.template.Template;
import com.goniyo.notification.template.TemplateService;
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
    private NotificationHandler<Email> notificationHandler;
    @Autowired
    private MessageGenerator messageGenerator;
    @Autowired
    private EmailNotifier<Email> emailNotifier;
    @Autowired
    private NotificationPersistence<Email> notificationPersistence; // TODO here?
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private TemplateService templateService;
    // TODO add validation here
    public Mono<NotificationStatusResponse> send(EmailDto emailDto) {
        log.debug("Preparing data for Email Notification {}", emailDto);
        String message = null;
        String id = emailDto.getBody().getTemplateId();
        Template template = getTemplate(id);
        try {


            message = messageGenerator.generateMessage(template.getBody(), emailDto);

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

    private Template getTemplate(String id) {
        // TODO remove block
        Template template = templateService.get(id).block();
        return template;
    }

    @Override
    public Flux<NotificationMessageDto> getAllEmails() {
        return notificationPersistence.getAll();
    }


}
