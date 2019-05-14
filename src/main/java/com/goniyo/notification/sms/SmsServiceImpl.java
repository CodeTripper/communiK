package com.goniyo.notification.sms;

import com.goniyo.notification.messagegenerator.MessageGenerationException;
import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import com.goniyo.notification.notification.NotificationStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Slf4j
class SmsServiceImpl implements SmsService {
    @Autowired
    NotificationHandler<Sms> notificationHandler;
    @Autowired
    MessageGenerator messageGenerator;
    @Autowired
    SmsNotifier<Sms> smsNotifier;
    @Resource(name = "OTP")
    SmsNotifier<Sms> optNotifier;
    // add validation here
    public String sendSms(SmsDto smsDto) {
        log.debug("About to send Sms");
        return send(smsDto, smsNotifier);

    }

    @Override
    public String sendOtp(SmsDto smsDto) {
        log.debug("About to send Otp");
        return send(smsDto, optNotifier);

    }

    private String send(SmsDto smsDto, SmsNotifier<Sms> optNotifier) {
        String message = null;
        try {
            message = messageGenerator.generateMessage("sms/salary-credited.ftl", smsDto);
        } catch (MessageGenerationException e) {
            e.printStackTrace();
        }
        Sms sms = Sms.builder().message(message).build();
        return notificationHandler.sendNotification(optNotifier, sms);
    }

    @Override
    public NotificationStatusResponse getSmsStatus(String id) {
        //notificationHandler.getNotificationStatus(id);
        return null;
    }

}
