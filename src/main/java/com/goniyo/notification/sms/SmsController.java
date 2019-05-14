package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@EnableAutoConfiguration
@Slf4j
public class SmsController {
    SmsService smsService;

    @PostMapping("/sms")
    public String sms(@Valid @RequestBody SmsDto smsDto) {
        log.debug("Inside SMSController");
        return smsService.sendSms(smsDto);
    }

    @PostMapping("/otp")
    public String otp(@Valid @RequestBody SmsDto smsDto) {
        // TODO json
        log.debug("Inside otp SMSController");
        return smsService.sendSms(smsDto);
    }

    @GetMapping("/sms/{id}")
    public String getSmsStatus(@PathVariable String id) {
        NotificationStatusResponse notificationResponse = smsService.getSmsStatus(id);
        return "SUCCESS";
    }
}

