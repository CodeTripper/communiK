package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@EnableAutoConfiguration
public class SmsController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
    @Resource(name = "APPSMS")
    SmsService smsService;

    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public String sms() {
        // TODO json
        logger.debug("Inside SMSController");

        SmsDto smsDTO = new SmsDto();
        smsDTO.setTo("99");
        return smsService.sendSms(smsDTO);
    }

    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    public String getSmsStatus(String id) {

        NotificationResponse notificationResponse = smsService.getSmsStatus(id);
        return "SUCCESS";
    }
}

