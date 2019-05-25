package in.codetripper.notification.sms;

import in.codetripper.notification.messagegenerator.MessageGenerationException;
import in.codetripper.notification.messagegenerator.MessageGenerator;
import in.codetripper.notification.notification.*;
import in.codetripper.notification.template.NotificationTemplate;
import in.codetripper.notification.template.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component
class SmsServiceImpl implements SmsService {
    @Autowired
    private TemplateService templateService;
    @Autowired
    Notification<Sms> notificationHandler;
    @Autowired
    MessageGenerator messageGenerator;
    @Autowired
    SmsNotifier<Sms> smsNotifier;
    @Resource(name = "OTP")
    SmsNotifier<Sms> optNotifier;
    @Autowired
    private SmsMapper smsMapper;

    // add validation here
    public Mono<NotificationStatusResponse> sendSms(SmsDto smsDto) {
        log.debug("About to send Sms");
        return send(smsDto, smsNotifier);

    }

    @Override
    public Mono<NotificationStatusResponse> sendOtp(SmsDto smsDto) {
        log.debug("About to send Otp");
        return send(smsDto, optNotifier);

    }

    private Mono<NotificationStatusResponse> send(SmsDto smsDto, SmsNotifier<Sms> notifier) {
        // TODO CACHE
        // validate all data present in SMS dto?
        return templateService.get(smsDto.getTemplateId()).
                single().
                map(template -> validateTemplate(template)).
                onErrorMap(original -> new InvalidRequestException("Invalid Request", original)).
                map(t -> generateMessage(t, smsDto)).
                map(message -> getSms(smsDto, notifier, message)).
                flatMap(sms -> notificationHandler.sendNotification(sms)).
                doOnError(err -> log.error("Error while sending SMS"));
    }

    private Sms getSms(SmsDto smsDto, SmsNotifier<Sms> notifier, String body) {
        Sms sms = smsMapper.smsDtoToSms(smsDto);
        sms.setBodyTobeSent(body);
        NotificationMessage.Notifiers notifiers = new NotificationMessage.Notifiers();
        notifiers.setPrimary(notifier);
        //notifiers.setBackup();
        sms.setNotifiers(notifiers);
        NotificationMessage.Meta meta = new NotificationMessage.Meta();
        // get IP
        meta.setSenderIp(null);
        meta.setType(Type.SMS);
        // meta.setCategory(template.getCategory());
        //meta.setLob(template.getLob());
        meta.setCreated(LocalDateTime.now());
        sms.setMeta(meta);
        return sms;
    }

    private NotificationTemplate validateTemplate(NotificationTemplate template) {
        log.debug("validating {}", template);
        if (template == null) {
            throw new InvalidRequestException("Template not found");
        }
        if (!template.getType().toString().equalsIgnoreCase(Type.SMS.toString())) {
            throw new InvalidRequestException("Notification type and Template mismatch");
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
        try {
            if (smsDto.getTemplateId().isEmpty()) {
                message = smsDto.getBody().getMessage();
            } else {
                message = messageGenerator.generateBlockingMessage(template.getBody(), smsDto);
            }
        } catch (MessageGenerationException e) {
            e.printStackTrace();
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
