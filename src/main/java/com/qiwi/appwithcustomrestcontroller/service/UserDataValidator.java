package com.qiwi.appwithcustomrestcontroller.service;


import com.qiwi.appwithcustomrestcontroller.exeption.DataValidationException;
import com.qiwi.appwithcustomrestcontroller.model.User;

public interface UserDataValidator {

    void validate(User user) throws DataValidationException;

}