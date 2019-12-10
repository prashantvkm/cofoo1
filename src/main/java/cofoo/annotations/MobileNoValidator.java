package cofoo.annotations;

import java.math.BigInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileNoValidator implements ConstraintValidator<MobileNo, String> {

    @Override
    public void initialize(MobileNo mobileNo) {
    }

    @Override
    public boolean isValid(String mobileNo, ConstraintValidatorContext constraintValidatorContext) {
        return mobileNo != null && mobileNo.matches("[0-9]+")
                && (mobileNo.length() >9) && (mobileNo.length() < 14);
    }
}
