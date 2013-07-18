package com.swooli.web.config.format;

import com.swooli.bo.user.Gender;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class GenderFormatter implements Formatter<Gender> {

    @Override
    public String print(Gender gender, Locale locale) {
        if (gender == null) {
            return Gender.DEFAULT.getDisplayName();
        }
        return gender.getDisplayName();
    }

    @Override
    public Gender parse(String formatted, Locale locale) throws ParseException {
        final Gender gender = Gender.valueOfByDisplayName(formatted);
        if (gender == null) {
            return Gender.DEFAULT;
        }
        return gender;
    }

}
