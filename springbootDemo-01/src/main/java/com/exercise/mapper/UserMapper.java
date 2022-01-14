package com.exercise.mapper;

import com.exercise.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select * from tb_user")
    List<User> selectUserList();

    @Select("select * from tb_user where id=#{id}")
    User selectOneUser(Long id);

    @Select("select * from tb_user where username=#{userName}")
    User selectOneUserByName(String userName);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into tb_user(username, password, email, gender, age) values (#{username}, #{password}, #{email}, #{gender}, #{age})")
    int insert(User user);
}
