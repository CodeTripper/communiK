package com.goniyo.notification.email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    EmailService emailService;

    @RequestMapping("/test")
    public String home() {
        return "Springboot";
    }

    @RequestMapping("/email")
    public String email() {
        // TODO immutables
        EmailDto emailDto = new EmailDto();
        emailDto.setTo("99");
        return emailService.sendEmail(emailDto);
    }
}

