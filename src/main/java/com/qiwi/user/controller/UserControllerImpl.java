package com.qiwi.user.controller;

import com.qiwi.servlet.annotation.audit.Audit;
import com.qiwi.user.model.User;
import com.qiwi.user.service.UserService;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestBody;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestMapping;
import com.qiwi.servlet.annotation.requestmapping.QiwiRestController;
import com.qiwi.servlet.annotation.requestmapping.QiwiPathVariable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Audit
@QiwiRestController
public class UserControllerImpl implements UserController{

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @QiwiRequestMapping(path = "/users", method = "GET")
    public List getUsers() {
        return userService.getAll();
    }

    @Override
    @QiwiRequestMapping(path = "/users/([0-9]+)", method = "GET")
    public User getUser(@QiwiPathVariable Long id) {
        return userService.get(id);
    }

    @Override
    @QiwiRequestMapping(path = "/users/([0-9]+)", method = "DELETE")
    public void deleteUser(@QiwiPathVariable Long id) {
        userService.delete(id);
    }

    @Override
    @QiwiRequestMapping(path = "/users/([0-9]+)", method = "PUT")
    public User updateUser(@QiwiPathVariable Long id, @QiwiRequestBody User user) {
        return userService.update(id, user);
    }

    @Override
    @QiwiRequestMapping(path = "/users", method = "POST")
    public User createUser(@QiwiRequestBody User user) {
        return userService.create(user);
    }

}
