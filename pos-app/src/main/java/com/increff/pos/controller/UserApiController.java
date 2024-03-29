package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Api
@RestController
public class UserApiController {
    @Autowired
    private UserDto dto;

    @ApiOperation(value = "Adds a User")
    @RequestMapping(path = "/session/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView add(HttpServletRequest req, UserForm form) throws ApiException {
        return dto.add(form);
    }
}
