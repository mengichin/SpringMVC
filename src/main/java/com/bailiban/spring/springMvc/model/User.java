package com.bailiban.spring.springMvc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private Integer id;
    @NotEmpty(message = "用户名不能为空")
    private String name;

    @Size(min = 8,max = 16,message = "密码至少为8位")
    private String password;

    private Integer sex;

    @Pattern(regexp = "^1[35678]\\d{9}$",message = "手机号格式不正确")
    private String phone;

    private String address;

    @Email(message = "邮箱格式不正确")
    private String mail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8" )
    private Date birthday;


    public User(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }


}
