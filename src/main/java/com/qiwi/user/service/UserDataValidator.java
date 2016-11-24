package com.qiwi.user.service;


import com.qiwi.user.exeption.DataValidationException;
import com.qiwi.user.model.User;

public interface UserDataValidator {

    void validate(User user) throws DataValidationException;

}