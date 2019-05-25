package in.codetripper.communik.template;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface NotificationTemplateMapper {

    NotificationTemplateRepoDto templateToTemplateRepoDto(NotificationTemplate notificationTemplate);

    NotificationTemplate templateRepoDtotoTemplate(NotificationTemplateRepoDto templateRepoDto);

    NotificationTemplateDto templateToTemplateDto(NotificationTemplate notificationTemplate);

    NotificationTemplate templateDtoToTemplate(NotificationTemplateDto templateDto);
}