package com.kob.backend.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用lombok的注解
@Data//自动创建getter、setter
@NoArgsConstructor//自动创建无参构造函数
@AllArgsConstructor//自动创建有参构造函数
public class User {
    private Integer id;
    private String username;
    private String password;
}
