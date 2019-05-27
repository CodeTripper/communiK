package in.codetripper.communik.email;

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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
class EmailServiceImpl implements EmailService {
    @Autowired
    private Notification<Email> notificationHandler;
    @Autowired
    private MessageGenerator messageGenerator;
    @Autowired
    private Map<String, EmailNotifier<Email>> providers;
    @Autowired
    private NotificationPersistence<Email> notificationPersistence; // TODO here?
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private NotificationTemplateService templateService;

    public Mono<NotificationStatusResponse> send(EmailDto emailDto) {
        return templateService.get(emailDto.getTemplateId()).
                single().
                map(this::validateTemplate).
                onErrorMap(original -> new InvalidRequestException("Invalid Request", original)).
                map(template -> generateMessage(template, emailDto)).
                map(message -> getEmail(emailDto, message)).
                flatMap(notificationHandler::sendNotification).
                doOnError(err -> log.error("Error while sending Email", err));

    }

    @Override
    public Flux<NotificationMessageRepoDto> getAllEmails() {
        return notificationPersistence.getAll();
    }

    private Email getEmail(EmailDto emailDto, String body) {
        log.debug("email called with provider {} for providers {}", emailDto.getProviderName(), providers);
        Email email = emailMapper.emailDtoToEmail(emailDto);
        //email.setSubject(emailDto.getSubject());
        email.setBodyTobeSent(body);
        NotificationMessage.Notifiers notifiers = new NotificationMessage.Notifiers();
        Optional<Entry<String, EmailNotifier<Email>>> defaultProvider = providers.entrySet().stream().
                filter(provider -> provider.getValue().isDefault()).
                findFirst();

        EmailNotifier<Email> requestedProvider = providers.get(emailDto.getProviderName());
        if (requestedProvider == null) {
            requestedProvider = defaultProvider.get().getValue();
        }
        List<EmailNotifier<Email>> backups = providers.entrySet().stream().
                filter(provider -> provider.getKey().equalsIgnoreCase(defaultProvider.get().getKey())).
                map(Entry::getValue).
                collect(Collectors.toList());

        notifiers.setPrimary(requestedProvider);
        notifiers.setBackup(backups);
        email.setNotifiers(notifiers);
        NotificationMessage.Meta meta = new NotificationMessage.Meta();
        // get IP
        meta.setSenderIp(null);
        meta.setType(getType());
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
        if (!template.getType().equals(getType())) {
            throw new InvalidRequestException("Notification type and Template mismatch");
        }
        return template;
    }


    private String generateMessage(NotificationTemplate template, EmailDto emailDto) {
        String message = "";
        if (emailDto.getTemplateId().isEmpty()) {
            message = emailDto.getBody().getMessage();
        } else {
            message = messageGenerator.generateBlockingMessage(template.getBody(), emailDto);
        }

        log.debug("generated message {}", message);
        return message;

    }

}
