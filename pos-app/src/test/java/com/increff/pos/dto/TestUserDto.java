package com.increff.pos.dto;

import com.increff.pos.model.UserForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestUserDto extends AbstractUnitTest{
    @Autowired
    private final UserDto userDto = new UserDto();

    @Autowired
    private final UserService userService = new UserService();
    @Test
    public void testAdd() throws ApiException {
        UserForm f = new UserForm();
        f.setEmail("jaynit@gmail.com");
        f.setPassword("1234");
        userDto.add(f);

        Assert.assertEquals(1,userService.getAll().size());
    }

    @Test
    public void testSupervisorRole() throws ApiException {
        UserForm f = new UserForm();
        f.setEmail("jaynit@gmail.com");
        f.setPassword("1234");
        userDto.add(f);
        Assert.assertEquals("supervisor",userService.getAll().get(0).getRole());
    }

    @Test
    public void testOperatorRole() throws ApiException {
        UserForm f = new UserForm();
        f.setEmail("jaynit1106@gmail.com");
        f.setPassword("1234");
        userDto.add(f);
        Assert.assertEquals("operator",userService.getAll().get(0).getRole());
    }
}
