package com.lp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lp.configuration.CustomObjectMapper;
import com.lp.entity.YdlLoginUser;
import com.lp.entity.YdlUser;
import com.lp.service.YdlUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LuoPing
 * @date 2022/10/7 19:51
 */
@RestController
@Slf4j
public class IndexController {

    @Resource
    private YdlUserService ydlUserService;
//    登陆
    @PostMapping("login")
    public ResponseEntity<YdlLoginUser> login(@RequestBody @Validated YdlUser ydlUser, BindingResult bindingResult) {
//        log.info("user="+ydlUser);
//        CustomObjectMapper customObjectMapper = new CustomObjectMapper();
//        try {
//            String s = customObjectMapper.writeValueAsString(ydlUser);
//            log.info(s);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        //处理不合法数据
        List<ObjectError> allErrors = bindingResult.getAllErrors();//获取所有登陆时发生的错误
        allErrors.forEach(error -> log.error("用户校验失败:{}",error.getDefaultMessage()));
        //判断是否大于0
        if (allErrors.size() >0){
            return ResponseEntity.status(500).build();//返回500状态
        }

        //执行登陆逻辑
        YdlLoginUser ydlLoginUser = null;
        try {
            ydlLoginUser = ydlUserService.login(ydlUser.getUserName(),ydlUser.getPassword());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().body(ydlLoginUser);
    }
//    退出
    @GetMapping("logout")
    public ResponseEntity<Boolean> logout(){
        try {
            ydlUserService.logout();
//            log.info(String.valueOf(res));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().body(true);
    }
}
