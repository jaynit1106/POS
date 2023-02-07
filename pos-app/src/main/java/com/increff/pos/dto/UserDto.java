package com.increff.pos.dto;

import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;


@Component
public class UserDto {
    @Value("${supervisor.email}")
    private  String email;
    @Autowired
    private  UserService userService;

    public ModelAndView add(UserForm form) throws ApiException {
        UserPojo p = ConvertUtil.objectMapper(form, UserPojo.class);
        if(Objects.equals(email,p.getEmail()))p.setRole("Supervisor");
        else p.setRole("Operator");
        return userService.add(p);
    }
}
