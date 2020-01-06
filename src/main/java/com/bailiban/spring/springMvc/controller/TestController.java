package com.bailiban.spring.springMvc.controller;

import com.bailiban.spring.springMvc.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/user")
   public String uesr(){
        return "hi";
    }
}
