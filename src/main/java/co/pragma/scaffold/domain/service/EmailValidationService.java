package co.pragma.scaffold.domain.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidationService {

    private static final Pattern PATTERN_EMAIL = Pattern.compile("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");


    public boolean isValidEmail(String email) {
        return email != null && PATTERN_EMAIL.matcher(email).matches();
    }
}
