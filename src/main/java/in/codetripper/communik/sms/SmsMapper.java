package in.codetripper.communik.sms;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface SmsMapper {
    Sms smsDtoToSms(SmsDto smsDto);

    SmsDto smsToSmsDto(Sms sms);

}