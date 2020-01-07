package com.bailiban.spring.springMvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private Integer id;
    private String name;
    private Date date;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
