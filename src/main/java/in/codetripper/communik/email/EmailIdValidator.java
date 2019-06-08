package in.codetripper.communik.email;

import java.util.Collection;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

public class EmailIdValidator implements ConstraintValidator<EmailList, Collection<String>> {

  private final EmailValidator validator = new EmailValidator();

  @Override
  public void initialize(EmailList emailList) {

  }

  @Override
  public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    // TODO use Java 8
    for (String s : value) {
      if (!validator.isValid(s, context)) {
        return false;
      }
      if (s.length() > 319) {
        return false;
      }
    }
    return true;
  }


}
