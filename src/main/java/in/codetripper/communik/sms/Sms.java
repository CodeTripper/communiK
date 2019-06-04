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
package in.codetripper.communik.sms;

import in.codetripper.communik.notification.NotificationMessage;
import lombok.Getter;

@Getter
public class Sms extends NotificationMessage {

    private long mobileNo;
    private long countryCode;

    // TODO change the below hack to Superbuilder when milestone 25 is released Idea plugin
    // https://github.com/mplushnikov/lombok-intellij-plugin/milestone/31
    /*
     * @Builder public Sms(Type type, String message, String to, String senderIp, Status status, long
     * mobileNo, long countryCode,String templateId) { super(type, message, to, senderIp,
     * status,templateId); this.mobileNo = mobileNo; this.countryCode = countryCode; }
     */
}
