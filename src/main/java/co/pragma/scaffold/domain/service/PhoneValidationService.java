package co.pragma.scaffold.domain.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PhoneValidationService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{7,10}$");

    public boolean isValidPhone(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches() &&
                containsOnlyDigits(phoneNumber);
    }

    private boolean containsOnlyDigits(String str) {
        return str.chars().allMatch(Character::isDigit);
    }
}
