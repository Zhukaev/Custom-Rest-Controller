package com.qiwi.Application.spring;

import com.qiwi.servlet.dispatcher.DispatcherServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ServerStarted{

    @Autowired
    private DispatcherServlet dispatcherServlet;

    @PostConstruct
    public void serverStart() throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(dispatcherServlet), "/*");

        server.start();
        //server.join();
    }

}
