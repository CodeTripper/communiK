package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class SmsController {
    @Autowired
    SmsAdapter smsFacade;

    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public String sms() {
        // TODO json
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setTo("99");
        return smsFacade.sendSms(smsDTO);
    }

    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    public String getSmsStatus(String id) {
        NotificationResponse notificationResponse = smsFacade.getSmsStatus(id);
        return "SUCCESS";
    }
}

