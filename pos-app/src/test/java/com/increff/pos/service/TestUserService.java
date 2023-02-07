package com.increff.pos.service;

import com.increff.pos.pojo.UserPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestUserService extends AbstractUnitTest{
    @Autowired
    private  UserService userService ;

    @Test
    public void testAdd() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail("jaynit@gmail.com");
        p.setPassword("1234");
        p.setRole("operator");
        userService.add(p);

        Assert.assertEquals(1,userService.getAll().size());
    }

    @Test
    public void testGetAll() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail("jaynit@gmail.com");
        p.setPassword("1234");
        p.setRole("operator");
        userService.add(p);

        p = new UserPojo();
        p.setEmail("jaynit1@gmail.com");
        p.setPassword("1234");
        p.setRole("operator");
        userService.add(p);

        p = new UserPojo();
        p.setEmail("jaynit2@gmail.com");
        p.setPassword("1234");
        p.setRole("operator");
        userService.add(p);

        Assert.assertEquals(3,userService.getAll().size());
    }

    @Test
    public void testGetByEmail() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail("jaynit@gmail.com");
        p.setPassword("1234");
        p.setRole("operator");
        userService.add(p);

        UserPojo pojo = userService.get("jaynit@gmail.com");
        Assert.assertEquals(pojo.getEmail(),"jaynit@gmail.com");
        Assert.assertEquals(pojo.getPassword(),"1234");
        Assert.assertEquals(pojo.getRole(),"operator");
    }
}
