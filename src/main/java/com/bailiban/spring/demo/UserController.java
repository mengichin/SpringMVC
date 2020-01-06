package com.bailiban.spring.demo;


import java.util.ArrayList;
import java.util.List;
@MyRestController
public class UserController {
    private static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User(1, "Jim"));
        userList.add(new User(2, "Lily"));
    }

    @MyRequestMapping("/get")
    public String get(int id) {
        return userList.stream().filter(u -> u.getId() == id).findAny().orElse(null).toString();
    }

    @MyRequestMapping("/getAll")
    public String getAll() {
        return userList.toString();
    }


}
