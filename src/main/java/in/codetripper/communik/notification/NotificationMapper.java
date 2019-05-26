package in.codetripper.communik.notification;

import in.codetripper.communik.email.Email;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import in.codetripper.communik.sms.Sms;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface NotificationMapper {


    NotificationMessage mapDtoToMessage(NotificationMessageRepoDto notificationMessageDto);

    NotificationMessageRepoDto mapMessageToDto(Email email);

    NotificationMessageRepoDto mapMessageToDto(Sms email);

    default NotificationMessageRepoDto mapMessageToDto(NotificationMessage notificationMessage) {
        if (notificationMessage instanceof Email) {
            return mapMessageToDto((Email) notificationMessage);
        } else if (notificationMessage instanceof Sms) {
            return mapMessageToDto((Sms) notificationMessage);
        } else {
            return null;
        }
    }

}