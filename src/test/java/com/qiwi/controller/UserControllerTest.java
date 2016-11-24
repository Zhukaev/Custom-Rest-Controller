package com.qiwi.controller;

import com.qiwi.Application.Application;
import com.qiwi.user.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class UserControllerTest {

    public static final String URL = "http://localhost:8080/users";
    private RestTemplate restTemplate = new RestTemplate();

    @After
    public void afterEachTest() throws IOException {
        String jsonString = restTemplate.getForObject(URL, String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        for (User user : users) {
            restTemplate.delete(URL + "/" + user.getId(), User.class);
        }
    }

    @Test
    public void testCreateNewUser() {
        User user = new User("87461315100", "Aer", "Das");
        User createdUser = restTemplate.postForObject(URL, user, User.class);

        Assert.assertEquals(user.getPhone(), createdUser.getPhone());
        Assert.assertEquals(user.getFirstName(), createdUser.getFirstName());
        Assert.assertEquals(user.getLastName(), createdUser.getLastName());
        Assert.assertNotNull(createdUser.getId());
    }

    @Test
    public void testGetUser() {
        User user = new User("87461315100", "Aer", "Das");
        User createdUser = restTemplate.postForObject(URL, user, User.class);

        User gotUser = restTemplate.getForObject(URL + "/" + createdUser.getId(), User.class);

        Assert.assertEquals(user.getPhone(), gotUser.getPhone());
        Assert.assertEquals(user.getFirstName(), gotUser.getFirstName());
        Assert.assertEquals(user.getLastName(), gotUser.getLastName());
        Assert.assertNotNull(gotUser.getId());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("87461315100", "Aer", "Das");
        User createdUser = null;
        try {
            createdUser = restTemplate.postForObject(URL, user, User.class);
            restTemplate.delete(URL + "/" + createdUser.getId(), User.class);
        } catch (HttpStatusCodeException e) {

            Assert.fail(e.getMessage() + ". Body: " + e.getResponseBodyAsString());
        }
        try {
            User deletedUser = restTemplate.getForObject(URL + "/" + createdUser.getId(), User.class);
            Assert.fail();
        } catch (HttpClientErrorException e) {

        }
    }

    @Test
    public void testGetManyUsers() throws IOException {
        List<User> users = new ArrayList<User>();
        users.add(new User("87461315100", "Aer", "Das"));
        users.add(new User("87461325100", "Ssd", "Sgh"));

        for (User user : users) {
            restTemplate.postForObject(URL, user, User.class);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = restTemplate.getForObject(URL, String.class);
        List<User> createdUsers = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        int i = 0;
        for (User createdUser : createdUsers) {
            Assert.assertNotNull(createdUser.getId());
            Assert.assertEquals(createdUser.getPhone(), users.get(i).getPhone());
            Assert.assertEquals(createdUser.getFirstName(), users.get(i).getFirstName());
            Assert.assertEquals(createdUser.getLastName(), users.get(i).getLastName());
            i++;
        }
    }

    @Test
    public void testUpdateUser() {
        User user = new User("87461315100", "Aer", "Das");
        User createdUser = restTemplate.postForObject(URL, user, User.class);

        User user2 = new User("87461325100", "Ssd", "Sgh");

        restTemplate.put(URL + "/" + createdUser.getId(), user2);
        User updatedUser = restTemplate.getForObject(URL + "/" + createdUser.getId(), User.class);

        Assert.assertEquals(createdUser.getId(), updatedUser.getId());
        Assert.assertEquals(user2.getPhone(), updatedUser.getPhone());
        Assert.assertEquals(user2.getFirstName(), updatedUser.getFirstName());
        Assert.assertEquals(user2.getLastName(), updatedUser.getLastName());
    }
}
