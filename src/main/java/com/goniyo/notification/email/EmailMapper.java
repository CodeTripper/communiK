package com.goniyo.notification.email;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface EmailMapper {
    Email emailDtoToEmail(EmailDto emailDto);

    EmailDto emailToEmailDto(Email email);

}