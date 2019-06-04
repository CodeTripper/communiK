/*
 * Copyright 2019 CodeTripper
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
