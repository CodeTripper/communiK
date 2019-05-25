package in.codetripper.communik.provider;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProviderMapper {
    Provider mapDtoToNotifier(ProviderDto notifierDto);

    ProviderDto mapNotifierToDto(Provider provider);

}