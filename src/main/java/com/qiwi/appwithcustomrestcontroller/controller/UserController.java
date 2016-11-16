package com.qiwi.appwithcustomrestcontroller.controller;

import com.qiwi.appwithcustomrestcontroller.model.User;
import com.qiwi.appwithcustomrestcontroller.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public List getUsers() {
        return userService.getAll();
    }

    public User getUser(Long id) {
        return userService.get(id);
    }

    public void deleteUser(Long id) {
        userService.delete(id);
    }

    public User updateUser(Long id, User user) {
        return userService.update(id, user);
    }

    public User createUser(User user) {
        return userService.create(user);
    }

}
