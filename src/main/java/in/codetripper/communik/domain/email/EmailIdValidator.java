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
package in.codetripper.communik.domain.email;

import java.util.Collection;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

public class EmailIdValidator implements ConstraintValidator<EmailList, Collection<EmailId>> {

  private final EmailValidator validator = new EmailValidator();

  @Override
  public void initialize(EmailList emailList) {

  }

  @Override
  public boolean isValid(Collection<EmailId> value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    // TODO use Java 8
    for (EmailId s : value) {
      if (!validator.isValid(s.getId(), context)) {
        return false;
      }
      if (s.getId().length() > 319 || s.getName().length() > 319) {
        return false;
      }
    }
    return true;
  }


}
