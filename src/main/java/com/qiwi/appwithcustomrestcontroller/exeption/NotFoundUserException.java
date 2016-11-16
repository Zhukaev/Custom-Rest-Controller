package com.qiwi.appwithcustomrestcontroller.exeption;

public class NotFoundUserException extends UserException {

    public NotFoundUserException() {
    }

    public NotFoundUserException(long id)
    {
        super("Пользователь с id: " + id + " не найден");
    }
}
