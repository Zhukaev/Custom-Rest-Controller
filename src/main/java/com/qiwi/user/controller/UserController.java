package com.qiwi.user.controller;

import com.qiwi.servlet.annotation.audit.Audit;
import com.qiwi.servlet.annotation.requestmapping.QiwiPathVariable;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestBody;
import com.qiwi.servlet.annotation.requestmapping.QiwiRequestMapping;
import com.qiwi.servlet.annotation.requestmapping.QiwiRestController;
import com.qiwi.user.model.User;

import java.util.List;

@Audit
@QiwiRestController
public interface UserController {
    @QiwiRequestMapping(path = "/users", method = "GET")
    List getUsers();

    @QiwiRequestMapping(path = "/users/([0-9]+)", method = "GET")
    User getUser(@QiwiPathVariable Long id);

    @QiwiRequestMapping(path = "/users/([0-9]+)", method = "DELETE")
    void deleteUser(@QiwiPathVariable Long id);

    @QiwiRequestMapping(path = "/users/([0-9]+)", method = "PUT")
    User updateUser(@QiwiPathVariable Long id, @QiwiRequestBody User user);

    @QiwiRequestMapping(path = "/users", method = "POST")
    User createUser(@QiwiRequestBody User user);
}
