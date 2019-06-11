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
package in.codetripper.communik;

public final class Constants {

  public static final int DB_READ_TIMEOUT = 5000; // TODO move to config
  public static final int DB_WRITE_TIMEOUT = 3000;
  public static final int PROVIDER_TIMEOUT = 4000;
  public static final String CACHE_DEFAULT = "DEFAULT";
  public static final String CACHE_TEMPLATE = "TEMPLATE";
  public static final String TRACE_EMAIL_OPERATION_NAME = "email.send";
  public static final String APPLICATION_BASE_PACKAGE = "in.codetripper.communik";
}
