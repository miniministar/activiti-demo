package com.exercise.service;

import com.exercise.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void insert() {
        User user = new User();
        user.setUsername("jack");
        user.setPassword(encoder.encode("111111"));
        user.setEmail("123@qq.com");
        user.setGender(1);
        user.setAge(20);
        userService.insert(user);

        user.setId(null);
        user.setUsername("rose");
        userService.insert(user);

        user.setId(null);
        user.setUsername("tom");
        userService.insert(user);

        user.setId(null);
        user.setUsername("jerry");
        userService.insert(user);

    }

    @Test
    public void blanck(){
        boolean blank = StringUtils.isBlank("");
        System.out.println(blank);
    }

}