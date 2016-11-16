package com.qiwi.service;


import com.qiwi.Application.Application;
import com.qiwi.appwithcustomrestcontroller.exeption.DataValidationException;
import com.qiwi.appwithcustomrestcontroller.exeption.DuplicatePnoneException;
import com.qiwi.appwithcustomrestcontroller.exeption.NotFoundUserException;
import com.qiwi.appwithcustomrestcontroller.model.User;
import com.qiwi.appwithcustomrestcontroller.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void createNewUserWithGoodFields() {
        userService.create(new User("89161238765", "Aa", "Bb"));
        userService.create(new User("+79161238765", "Aa", "Bb"));
    }

    @Test
    public void createNewUserWithBadField() {
        chechFieldValidation(new User("89161238765", "aa", "Bb"));
        chechFieldValidation(new User("89161238765", "Aa", "bb"));
        chechFieldValidation(new User("89161238765", "aa", "bb"));
        chechFieldValidation(new User("89161238765", "a", "Bb"));
        chechFieldValidation(new User("89161238765", "aa", "B"));
        chechFieldValidation(new User("89161238765", "a", "b"));
        chechFieldValidation(new User("89161238765", "aa1", "Bb"));
        chechFieldValidation(new User("89161238765", "aa", "Bb1"));
        chechFieldValidation(new User("89161238765", "a@a", "Bb"));
        chechFieldValidation(new User("89161238765", "aa ", "Bb"));
    }

    private void chechFieldValidation(User user) {
        try {
            userService.create(user);
            Assert.fail();
        } catch (DataValidationException e) {
        }
    }

    @Test
    public void createNewUserWithBadPhone() {
        chechFieldValidation(new User("123", "Aa", "Bb"));
        chechFieldValidation(new User("812345678901", "Aa", "Bb"));
        chechFieldValidation(new User("asd", "Aa", "Bb"));
        chechFieldValidation(new User("79160123456", "Aa", "Bb"));
        chechFieldValidation(new User("+80123456789", "Aa", "Bb"));
        chechFieldValidation(new User("8 012 345 67 89", "Aa", "Bb"));
    }

    @Test
    public void createNewUserWithDuplicatePhone() {
        userService.create(new User("89161238765", "Aa", "Aa"));
        try {
            userService.create(new User("89161238765", "Bb", "Bb"));
            Assert.fail();
        } catch (DuplicatePnoneException e) {
        }
    }

    @Test
    public void getUser() {
        String phone = "81234567890";
        String firstname = "Aa";
        String lastname = "Aa";
        long id = userService.create(new User(phone, firstname, lastname)).getId();
        User user = userService.get(id);
        if (!(user.getId() != null && user.getFirstName().equals(firstname) && user.getLastName().equals(lastname) && user.getPhone().equals(phone)))
            Assert.fail();
    }

    @Test
    public void getDeletedUser() {
        long id = userService.create(new User("89101458765", "Aa", "Aa")).getId();
        userService.get(id);
        userService.delete(id);
        try {
            userService.get(id);
            Assert.fail();
        } catch (NotFoundUserException e) {
        }
    }

    @Test
    public void updateUserWithNotCorrectID() {
        try {
            userService.update(1, new User("89131238765", "Dd", "Dd"));
            Assert.fail();
        } catch (NotFoundUserException e) {
        }
    }

    @Test
    public void updateUserWithCorrectID() {
        long id = userService.create(new User("89101458765", "Aa", "Aa")).getId();
        userService.update(id, new User("89131238765", "Dd", "Dd"));
    }

    @Test
    public void updateUserWithDuplicatePhone() {
        userService.create(new User("89101458765", "Aa", "Aa"));
        long id = userService.create(new User("89111458765", "Aa", "Aa")).getId();
        try {
            userService.update(id, new User("89101458765", "Dd", "Dd"));
            Assert.fail();
        } catch (DuplicatePnoneException e) {
        }
    }

    @Test
    public void getAll() {
        userService.create(new User("89101458765", "Aa", "Aa"));
        userService.create(new User("89111238765", "Bb", "Bb"));
        userService.create(new User("89121238765", "Cc", "Cc"));
        userService.create(new User("89131238765", "Dd", "Dd"));
        List users = userService.getAll();
        Assert.assertEquals(4,users.size());
    }

    @Test
    public void deleteUserWithNotCorrectID() {
        try {
            userService.delete(1);
            Assert.fail();
        } catch (NotFoundUserException e) {
        }
    }
}