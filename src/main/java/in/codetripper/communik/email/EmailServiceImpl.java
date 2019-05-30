package in.codetripper.communik.email;

import in.codetripper.communik.exceptions.InvalidRequestException;
import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.messagegenerator.MessageGenerator;
import in.codetripper.communik.notification.Notification;
import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.NotificationPersistence;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import in.codetripper.communik.template.NotificationTemplate;
import in.codetripper.communik.template.NotificationTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static in.codetripper.communik.Constants.DB_READ_TIMEOUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.*;

@Component
@Slf4j
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {
    private final Notification<Email> notificationHandler;
    private final MessageGenerator<EmailDto> messageGenerator;
    private final Map<String, EmailNotifier<Email>> providers;
    private final NotificationPersistence<Email> notificationPersistence; // TODO here?
    private final EmailMapper emailMapper;
    private final NotificationTemplateService templateService;

    public Mono<NotificationStatusResponse> sendEmail(EmailDto emailDto) {
        return templateService.get(emailDto.getTemplateId()).
                timeout(Duration.ofMillis(DB_READ_TIMEOUT)).
                single().
                map(this::validateTemplate).
                onErrorMap(original -> {
                    if (original instanceof TimeoutException) {
                        return new NotificationPersistenceException(NOTIFICATION_PERSISTENCE_DB_TIMED_OUT);
                    } else {
                        return new InvalidRequestException(INVALID_REQUEST_TEMPLATE_NOT_FOUND);
                    }
                }).
                map(t -> generateMessage(t, emailDto)).
                map(message -> getEmail(emailDto, message)).
                flatMap(notificationHandler::sendNotification).
                doOnError(err -> log.error("Error while sending Email", err)).
                onErrorMap(original -> new NotificationSendFailedException(NOTIFICATION_SEND_FAILURE));

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
        NotificationMessage.Notifiers<Email> notifiers = new NotificationMessage.Notifiers<>();
        Optional<Entry<String, EmailNotifier<Email>>> defaultProvider = providers.entrySet().stream().
                filter(provider -> provider.getValue().isDefault()).
                findFirst();

        EmailNotifier<Email> requestedProvider = providers.get(emailDto.getProviderName());
        if (requestedProvider == null) {
            if (defaultProvider.isPresent()) {
                requestedProvider = defaultProvider.get().getValue();
            } else {
                throw new RuntimeException(NO_DEFAULT_PROVIDER);
            }
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
        log.debug("validating template {}", template);
        if (template == null) {
            throw new InvalidRequestException(INVALID_REQUEST_TEMPLATE_NOT_FOUND);
        }
        if (!template.getType().equals(getType())) {
            throw new InvalidRequestException(INVALID_REQUEST_TEMPLATE_MISMATCH);
        }
        return template;
    }


    private String generateMessage(NotificationTemplate template, EmailDto emailDto) {
        String message;
        if (emailDto.getTemplateId().isEmpty()) {
            message = emailDto.getBody().getMessage();
        } else {
            message = messageGenerator.generateMessage(template.getBody(), emailDto);
        }

        log.debug("generated message {}", message);
        return message;

    }

}
