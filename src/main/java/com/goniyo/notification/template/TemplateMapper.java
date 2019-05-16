package com.goniyo.notification.template;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface TemplateMapper {

    TemplateRepoDto templateToTemplateRepoDto(Template template);

    Template templateRepoDtotoTemplate(TemplateRepoDto templateRepoDto);

    TemplateDto templateToTemplateDto(Template template);

    Template templateDtoToTemplate(TemplateDto templateDto);
}