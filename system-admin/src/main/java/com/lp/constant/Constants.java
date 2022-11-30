package com.lp.constant;

/**
 * @author LuoPing
 * @date 2022/10/8 15:57
 */
public class Constants {
    //    统一的token前缀
    public static final String TOKEN_PREFIX = "token:";
    //    拦截器请求中authorization前缀
    public static final String HEAD_AUTHORIZATION = "Authorization";
    //    增加token失效时间
    public static final long TOKEN_TIME = 30*60L;
    //角色前缀
    public static final String ROLE_PREFIX = "roles:";
    //权限前缀
    public static final String PERM_PREFIX = "perm:";
    //防止重复提交的key前缀
    public static final String REPEAT_SUBMIT_KEY = "repeat:";
}
