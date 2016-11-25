package com.qiwi.user.controller;

import com.qiwi.servlet.annotation.audit.Audit;
import com.qiwi.servlet.annotation.requestmapping.QiwiPathVariable;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestBody;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestMapping;
import com.qiwi.servlet.annotation.requestmapping.QiwiRestController;
import com.qiwi.user.model.User;

import java.util.List;

public interface UserController {
    List getUsers();

    User getUser(Long id);

    void deleteUser(Long id);

    User updateUser(Long id, User user);

    User createUser(User user);
}
