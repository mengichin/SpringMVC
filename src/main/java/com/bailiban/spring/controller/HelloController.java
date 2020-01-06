package com.bailiban.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class HelloController {
    @RequestMapping({"/", "", "/hello"})
    public String hello() {
        return "Hello World!";
    }


}
