package com.bailiban.spring.springMvc.controller;

import com.bailiban.spring.springMvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/user1")
   public User getUser(@RequestBody User user){
        System.out.println(user);
        return user;
    }

}
