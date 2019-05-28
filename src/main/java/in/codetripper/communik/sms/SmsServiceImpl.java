package in.codetripper.communik.sms;

import in.codetripper.communik.exceptions.InvalidRequestException;
import in.codetripper.communik.exceptions.NotificationPersistenceException;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.messagegenerator.MessageGenerator;
import in.codetripper.communik.notification.Notification;
import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.template.NotificationTemplate;
import in.codetripper.communik.template.NotificationTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import static in.codetripper.communik.Constants.DB_READ_TIMEOUT;
import static in.codetripper.communik.exceptions.ExceptionConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
class SmsServiceImpl implements SmsService {

    private final NotificationTemplateService templateService;
    private final Notification<Sms> notificationHandler;
    private final MessageGenerator<SmsDto> messageGenerator;
    private final SmsNotifier<Sms> smsNotifier;
    private final SmsMapper smsMapper;

    // add validation here
    public Mono<NotificationStatusResponse> sendSms(SmsDto smsDto) {
        log.debug("About to sendEmail Sms");
        return send(smsDto, smsNotifier);

    }

    @Override
    public Mono<NotificationStatusResponse> sendOtp(SmsDto smsDto) {
        log.debug("About to sendEmail Otp");
        return send(smsDto, smsNotifier);

    }

    private Mono<NotificationStatusResponse> send(SmsDto smsDto, SmsNotifier<Sms> notifier) {
        // TODO CACHE
        // validate all data present in SMS dto?
        return templateService.get(smsDto.getTemplateId()).
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
                map(t -> generateMessage(t, smsDto)).
                map(message -> getSms(smsDto, notifier, message)).
                flatMap(notificationHandler::sendNotification).
                onErrorMap(original -> new NotificationSendFailedException(NOTIFICATION_SEND_FAILURE));
        //doOnError(err -> log.error("Error while sending SMS",err));
    }

    private Sms getSms(SmsDto smsDto, SmsNotifier<Sms> notifier, String body) {
        Sms sms = smsMapper.smsDtoToSms(smsDto);
        sms.setBodyTobeSent(body);
        NotificationMessage.Notifiers<Sms> notifiers = new NotificationMessage.Notifiers<>();
        notifiers.setPrimary(notifier);
        //notifiers.setBackup();
        sms.setNotifiers(notifiers);
        NotificationMessage.Meta meta = new NotificationMessage.Meta();
        // get IP
        meta.setSenderIp(null);
        meta.setType(getType());
        // meta.setCategory(template.getCategory());
        //meta.setLob(template.getLob());
        meta.setCreated(LocalDateTime.now());
        sms.setMeta(meta);
        return sms;
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

    @Override
    public NotificationStatusResponse getSmsStatus(String id) {
        //notificationHandler.getNotificationStatus(id);
        return null;
    }

    private String generateMessage(NotificationTemplate template, SmsDto smsDto) {
        String message = "";
            if (smsDto.getTemplateId().isEmpty()) {
                message = smsDto.getBody().getMessage();
            } else {
                message = messageGenerator.generateBlockingMessage(template.getBody(), smsDto);
            }

        log.debug("generated message {}", message);
        return message;

    }

    //  //String message = "THERE IS A MAN";
    //        /*try {
    //            message = messageGenerator.generateMessage(template.getBody(), smsDto);
    //
    //        } catch (MessageGenerationException e) {
    //            e.printStackTrace();
    //        }*/

}
