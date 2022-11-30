package com.lp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lp.core.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class TestController {

    @Resource
    private RedisTemplate redisTemplate;
//    public User test(){
//        return new User("tom",12);
//    }
    @GetMapping("test")
    public String  test(){
        redisTemplate.setObject("map", List.of("ZS","lisi","ww"),-1L);
        List<String> list = redisTemplate.getObject("map", new TypeReference<>() {});
        log.info(list.toString());
        return "Hello ssm-pro";
    }


    static class User{

        String username;
        Integer age;

        public User(String username, Integer age) {
            this.username = username;
            this.age = age;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
