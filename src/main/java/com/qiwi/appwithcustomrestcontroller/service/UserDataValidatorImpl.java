package com.qiwi.appwithcustomrestcontroller.service;

import com.qiwi.appwithcustomrestcontroller.exeption.DataValidationException;
import com.qiwi.appwithcustomrestcontroller.model.User;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserDataValidatorImpl implements UserDataValidator {

    @Override
    public void validate(User user) {
        isValidPhone(user);
        isValidName(user.getFirstName());
        isValidName(user.getLastName());
    }

    private void isValidPhone(User user) throws DataValidationException {
        String phone = user.getPhone();
        Pattern pattern = Pattern.compile("^(8|\\+7)[\\d]{10}$");
        if (!pattern.matcher(phone).find())
            throw new DataValidationException("Некорректный номер телефона: " + phone + ". Корректный формат: +71234567890 или 81234567890");
    }

    private void isValidName(String name) throws DataValidationException {
        if (name.length() < 2 || name.length() > 25)
            throw new DataValidationException("Имя должно содержать от 2 до 25 символов");
        if (!Pattern.compile("^[A-Za-z]{1,25}$").matcher(name).matches())
            throw new DataValidationException("Имя должно содержать только буквы");
        if (!Pattern.compile("^[A-Z]{1}[a-z]{1,24}$").matcher(name).matches())
            throw new DataValidationException("Имя должно начинаться с большой буквы");
    }

}
