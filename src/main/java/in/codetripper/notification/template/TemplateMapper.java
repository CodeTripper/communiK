package in.codetripper.notification.template;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface TemplateMapper {

    TemplateRepoDto templateToTemplateRepoDto(NotificationTemplate notificationTemplate);

    NotificationTemplate templateRepoDtotoTemplate(TemplateRepoDto templateRepoDto);

    TemplateDto templateToTemplateDto(NotificationTemplate notificationTemplate);

    NotificationTemplate templateDtoToTemplate(TemplateDto templateDto);
}