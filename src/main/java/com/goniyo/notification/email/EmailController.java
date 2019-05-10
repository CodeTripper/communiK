package com.goniyo.notification.email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    EmailAdapter emailFacade;

    @RequestMapping("/test")
    public String home() {
        return "Springboot";
    }

    @RequestMapping("/email")
    public String email() {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo("99");
        return emailFacade.sendEmail(emailDto);
    }
}

