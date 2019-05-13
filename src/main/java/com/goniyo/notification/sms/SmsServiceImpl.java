package com.goniyo.notification.sms;

import com.goniyo.notification.messagegenerator.MessageGenerationException;
import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import com.goniyo.notification.notification.NotificationStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


class SmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
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
        logger.debug("About to send Sms");
        Sms sms = new Sms(); // map SmsDto()
        try {
            String message = messageGenerator.generateMessage("sms/salary-credited.ftl", smsDto);
        } catch (MessageGenerationException e) {
            e.printStackTrace();
        }
        return notificationHandler.sendNotification(smsNotifier, sms);

    }

    @Override
    public String sendOtp(SmsDto smsDto) {
        logger.debug("About to send Otp");
        Sms sms = new Sms(); // map SmsDto()
        try {
            String message = messageGenerator.generateMessage("sms/salary-credited.ftl", smsDto);
        } catch (MessageGenerationException e) {
            e.printStackTrace();
        }
        return notificationHandler.sendNotification(optNotifier, sms);

    }

    @Override
    public NotificationStatusResponse getSmsStatus(String id) {
        //notificationHandler.getNotificationStatus(id);
        return null;
    }

}
