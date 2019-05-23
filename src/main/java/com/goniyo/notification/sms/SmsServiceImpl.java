package com.goniyo.notification.sms;

import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.*;
import com.goniyo.notification.template.NotificationTemplate;
import com.goniyo.notification.template.TemplateService;
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
                map(m -> getSms(smsDto, notifier)).
                flatMap(r -> notificationHandler.sendNotification(r)).doOnError(err -> log.error("Error while sending SMS"));
    }

    private Sms getSms(SmsDto smsDto, SmsNotifier<Sms> notifier) {
        Sms sms = smsMapper.smsDtoToSms(smsDto);
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

    private Mono<Boolean> validateTemplate(NotificationTemplate template) {
        log.debug("validating {}", template);
        if (template == null) {
            throw new InvalidRequestException("Template not found");
        }
        if (!template.getType().toString().equalsIgnoreCase(Type.SMS.toString())) {
            throw new InvalidRequestException("Notification type and Template mismatch");
        }
        return Mono.empty();
    }

    @Override
    public NotificationStatusResponse getSmsStatus(String id) {
        //notificationHandler.getNotificationStatus(id);
        return null;
    }

    // TODO Cacheable Use CacheMono
    private Mono<NotificationTemplate> getTemplate(String id) {
        Mono<NotificationTemplate> template = templateService.get(id);
        return template;
    }

    //  //String message = "THERE IS A MAN";
    //        /*try {
    //            message = messageGenerator.generateMessage(template.getBody(), smsDto);
    //
    //        } catch (MessageGenerationException e) {
    //            e.printStackTrace();
    //        }*/

}
