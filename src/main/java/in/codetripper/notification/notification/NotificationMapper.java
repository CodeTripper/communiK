package in.codetripper.notification.notification;

import in.codetripper.notification.repository.mongo.NotificationMessageDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface NotificationMapper {


    NotificationMessage mapDtoToMessage(NotificationMessageDto notificationMessageDto);

    NotificationMessageDto mapMessageToDto(NotificationMessage notificationMessage);

}