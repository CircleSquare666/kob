package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserMapper userMapper;

    //可以指定是具体哪种请求方式
    @GetMapping("/user/all")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }
    @GetMapping("/user/users/{userId}")
    //@PathVariable指定具体变量
    public List<User> getUsers(@PathVariable int userId){
        //Mybatis-plus的条件构造器 gt 大于 lt 小于 ge 大于等于 le 小于等于
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.gt("id", userId - 1).lt("id", userId + 1);
        return userMapper.selectList(userQueryWrapper);
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable int userId){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",userId);
        return userMapper.selectOne(userQueryWrapper);
    }

    @GetMapping("/user/add/{username}/{password}")
    public String addUser(
            @PathVariable String username,
            @PathVariable String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(password);
        User user = new User(null, username, encode);
        userMapper.insert(user);
        return "Add User Successfully!";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable int userId){
        int i = userMapper.deleteById(userId);
        return "Delete User Successfully!";
    }
}
