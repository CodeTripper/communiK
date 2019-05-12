package com.goniyo.notification.sms;

import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import com.goniyo.notification.notification.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("APPOTP")
class OtpSmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(OtpSmsServiceImpl.class);
    @Autowired
    NotificationHandler<Sms> notificationHandler;
    @Autowired
    MessageGenerator messageGenerator;
    @Resource(name = "OTP")
    SmsNotifier<Sms> smsNotifier;

    // add validation here
    public String sendSms(SmsDto smsDTO) {
        logger.debug("Inside SMS Adaptor");
        Sms sms = new Sms(); // map SmsDto()
        String message = messageGenerator.generateMessage("", sms);
        return notificationHandler.sendNotification(smsNotifier, sms);

    }

    @Override
    public NotificationResponse getSmsStatus(String id) {
        notificationHandler.getNotificationStatus(id);
        return null;
    }

}
