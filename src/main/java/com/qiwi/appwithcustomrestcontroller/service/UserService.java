package com.qiwi.appwithcustomrestcontroller.service;

import com.qiwi.appwithcustomrestcontroller.exeption.DuplicatePnoneException;
import com.qiwi.appwithcustomrestcontroller.exeption.NotFoundUserException;
import com.qiwi.appwithcustomrestcontroller.model.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface UserService {

    User create(User user) throws DuplicatePnoneException;

    User get(long id) throws NotFoundUserException;

    void delete(long id) throws NotFoundUserException;

    User update(long id, User user) throws EmptyResultDataAccessException, DuplicatePnoneException;

    List<User> getAll() throws NotFoundUserException;
}
