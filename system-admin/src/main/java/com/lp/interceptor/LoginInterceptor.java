package com.lp.interceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lp.configuration.CustomObjectMapper;
import com.lp.constant.Constants;
import com.lp.core.RedisTemplate;
import com.lp.entity.YdlLoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 登陆拦截器
 * @author LuoPing
 * @date 2022/10/9 11:00
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CustomObjectMapper customObjectMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        设置一个白名单
//        application配置文件中已经注册有
//        List<String> whiteNames = List.of("/admin/login");
//        if(whiteNames.contains(request.getRequestURI()))//判断/admin/login对象是否存在于列表中
//            return true;
//        如果不是白名单的请求，检测
//        判断有没有Authorrization这个请求头，拿到首部信息的Authorization的值
//        401  由于缺乏目标资源要求的身份验证凭证
        ResponseEntity<String> res = ResponseEntity.status(401).body("Bad Credentials!");
//        log.info("res ="+res);
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);//获取请求头中的Authorization的值
//        处理token未失效时，刷新网页回到登陆页面问题
        if (token == null){//token等于空，给页面返回
            response.setStatus(401);
            response.getWriter().write(customObjectMapper.writeValueAsString(res));
            return false;
        }
        //        1、前端传username 拼接成 token:username:uuid 的数据
//        String tokenKey = Constants.TOKEN_PREFIX + request.getHeader("username")+":"+token;
//        log.info("token="+token);
//        2、后端根据token查询key
        Set<String> keys = redisTemplate.keys(Constants.TOKEN_PREFIX + "*" + token);
        if (keys == null || keys.size() == 0){
            response.setStatus(401);
            response.getWriter().write(customObjectMapper.writeValueAsString(res));
            return false;
        }
//        String tokenKey1 = keys.toString();
        String tokenKey = (String) keys.toArray()[0];
        log.info("key"+tokenKey);
//        通过token获取登陆用户
//        YdlLoginUser ydlLoginUser = redisTemplate.getObject(Constants.TOKEN_PREFIX + token, new TypeReference<>() {
//        });
        YdlLoginUser ydlLoginUser = redisTemplate.getObject(tokenKey, new TypeReference<>() {});
        log.info("token="+token+"ydlLoginUser="+ydlLoginUser);
        if (ydlLoginUser == null){//判断Redis中是否有该登陆用户
            response.setStatus(401);
            response.getWriter().write(customObjectMapper.writeValueAsString(res));
            return false;
        }
//        给token续命
//        redisTemplate.expire(Constants.TOKEN_PREFIX + token,Constants.TOKEN_TIME);
        redisTemplate.expire(tokenKey,Constants.TOKEN_TIME);
        return true;
    }
}
