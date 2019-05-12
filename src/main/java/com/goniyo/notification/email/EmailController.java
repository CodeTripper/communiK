package com.goniyo.notification.email;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Resource(name = "salaryCreditedEmailService")
    EmailService emailService;

    @RequestMapping("/test")
    public String home() {
        return "Springboot";
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public String email(@Valid @RequestBody EmailDto emailDto) {
        logger.debug("emaildto message{}", emailDto.getMessage());
        //return new ResponseEntity(customer, HttpStatus.OK);
        return emailService.sendEmail(emailDto);
    }
}

K