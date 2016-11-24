package com.qiwi.user.service;

import com.qiwi.user.dao.UserDAO;
import com.qiwi.user.exeption.DuplicatePnoneException;
import com.qiwi.user.exeption.NotFoundUserException;
import com.qiwi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserDataValidator userDataValidator;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, UserDataValidator userDataValidator) {
        this.userDAO = userDAO;
        this.userDataValidator = userDataValidator;
    }

    @Override
    public User get(long id) throws NotFoundUserException {
        return userDAO.get(id);
    }

    @Override
    public void delete(long id) throws NotFoundUserException {
        userDAO.delete(id);
    }

    @Override
    public User update(long id, User user) throws EmptyResultDataAccessException, DuplicatePnoneException {
        userDataValidator.validate(user);
        return userDAO.update(id, user);
    }

    @Override
    public List<User> getAll() throws NotFoundUserException {
        return userDAO.getAll();
    }

    @Override
    public User create(User user) {
        userDataValidator.validate(user);
        return userDAO.insert(user);
    }
}
