package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@EnableAutoConfiguration
public class SmsController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
    SmsService smsService;
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public String sms(@Valid @RequestBody SmsDto smsDto) {
        logger.debug("Inside SMSController");
        return smsService.sendSms(smsDto);
    }

    @RequestMapping(value = "/otp", method = RequestMethod.POST)
    public String otp(@Valid @RequestBody SmsDto smsDto) {
        // TODO json
        logger.debug("Inside otp SMSController");
        return smsService.sendSms(smsDto);
    }

    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    public String getSmsStatus(@PathVariable String id) {
        NotificationStatusResponse notificationResponse = smsService.getSmsStatus(id);
        return "SUCCESS";
    }
}

