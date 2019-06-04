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
package in.codetripper.communik.exceptions;

public interface ExceptionConstants {

  String INVALID_REQUEST_TEMPLATE_MISMATCH = "Notification type and Template mismatch";
  String INVALID_REQUEST_TEMPLATE_NOT_FOUND = "Template not found";
  String VALIDATION_EMAIL_INVALID_EMAIL = "Email should be valid";
  String VALIDATION_EMAIL_EMPTY_EMAIL = "Email should not be blank";
  String NOTIFICATION_PERSISTENCE_UNABLE_TO_SAVE = "Unable to save notification";
  String NOTIFICATION_PERSISTENCE_UNABLE_TO_UPDATE = "Unable to update notification";
  String NOTIFICATION_PERSISTENCE_ID_NOT_PRESENT =
      "Unable to update notification as id is not present";
  String NOTIFICATION_PERSISTENCE_DB_TIMED_OUT = "DB TimedOut";
  String NOTIFICATION_SEND_FAILURE = "Unable to send notification";
  String NO_DEFAULT_PROVIDER = "No default provider configured";
}
