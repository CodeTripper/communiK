package in.codetripper.communik.provider;

import in.codetripper.communik.repository.mongo.ProviderRepoDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProviderMapper {
    Provider mapDtoToNotifier(ProviderRepoDto notifierDto);

    ProviderRepoDto mapNotifierToDto(Provider provider);

}