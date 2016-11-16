package com.qiwi.appwithcustomrestcontroller.controller;


import com.qiwi.appwithcustomrestcontroller.model.User;
import com.qiwi.servlet.MethodDefinition;
import com.qiwi.servlet.RequestMapping;
import com.qiwi.servlet.RequestMappingImpl;
import com.qiwi.servlet.requestparam.PathVariable;
import com.qiwi.servlet.requestparam.RequestBody;
import com.qiwi.servlet.requestparam.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ControllerConfiguration {

    private final UserController controller;

    @Autowired
    public ControllerConfiguration(UserController controller) {
        this.controller = controller;
    }

    @Bean
    public RequestMapping methodsUserController() throws NoSuchMethodException {
        RequestMappingImpl methods = new RequestMappingImpl("/users/([0-9]+)");

        methods.addMethod("GET",
                new MethodDefinition(controller,
                        UserController.class.getMethod("getUser", new Class[] {Long.class}),
                        new ArrayList<RequestParam>(Arrays.asList(new PathVariable(Long.class)))
                )
        );

        methods.addMethod("PUT",
                new MethodDefinition(controller,
                        UserController.class.getMethod("updateUser", new Class[] {Long.class, User.class}),
                        new ArrayList<RequestParam>(Arrays.asList(new PathVariable(Long.class), new RequestBody(User.class)))
                )
        );

        methods.addMethod("DELETE",
                new MethodDefinition(controller,
                        UserController.class.getMethod("deleteUser", new Class[] {Long.class}),
                        new ArrayList<RequestParam>(Arrays.asList(new PathVariable(Long.class)))
                )
        );

        return methods;
    }

    @Bean
    public RequestMapping methodsUsersController() throws NoSuchMethodException {
        RequestMappingImpl methods = new RequestMappingImpl("/users");

        methods.addMethod("GET",
                new MethodDefinition(controller,
                        UserController.class.getMethod("getUsers", new Class[] {}),
                        null
                )
        );

        methods.addMethod("POST",
                new MethodDefinition(controller,
                        UserController.class.getMethod("createUser", new Class[] {User.class}),
                        new ArrayList<RequestParam>(Arrays.asList(new RequestBody(User.class)))
                )
        );

        return methods;
    }
}
