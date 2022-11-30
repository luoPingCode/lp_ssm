package com.lp.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lp.core.RedisTemplate;
import com.lp.constant.Constants;
import com.lp.entity.YdlLoginUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

/**
 * 获取当前的登陆对象
 * @author LuoPing
 * @date 2022/10/14 16:32
 */
public class AuthUtil {
    public static YdlLoginUser getLoginUser(RedisTemplate redisTemplate){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取首部信息的token
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);
        if (token == null) {
            throw new RuntimeException("当前用户未登录！");
        }
        Set<String> keys = redisTemplate.keys(Constants.TOKEN_PREFIX + "*" + token);
        if (keys== null || keys.size() == 0){
            throw new RuntimeException("当前用户未登录！");
        }
        String tokenKey = (String)keys.toArray()[0];
        // 3、使用token去redis中查看，有没有对应的loginUser
        return redisTemplate.getObject(tokenKey, new TypeReference<>() {});
    }
}
