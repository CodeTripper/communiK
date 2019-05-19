package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@EnableAutoConfiguration
@Slf4j
public class SmsController {
    @Autowired
    private SmsService smsService;

    @PostMapping("/sms")
    public Mono<NotificationMessage> sms(@Valid @RequestBody SmsDto smsDto) {
        log.debug("Inside SMSController");
        return smsService.sendSms(smsDto);
    }

    @PostMapping("/otp")
    public Mono<NotificationMessage> otp(@Valid @RequestBody SmsDto smsDto) {
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

