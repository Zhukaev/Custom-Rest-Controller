package com.qiwi.user.exeption;

public class DuplicatePnoneException extends UserException {
    public DuplicatePnoneException() {
    }

    public DuplicatePnoneException(String phone) {
        super("Пользователь с номером телефона: " + phone + " уже зарегистрирован");
    }
}
