package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
//用mybatis-plus要继承BaseMapper类来自动实现Mapper
public interface UserMapper extends BaseMapper<User> {

}
