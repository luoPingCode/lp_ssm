package com.lp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lp.configuration.CustomObjectMapper;
import com.lp.core.RedisTemplate;
import com.lp.constant.Constants;
import com.lp.entity.YdlLoginUser;
import com.lp.entity.YdlMenu;
import com.lp.entity.YdlRole;
import com.lp.entity.YdlUser;
import com.lp.dao.YdlUserDao;
import com.lp.exception.PasswordIncorrectException;
import com.lp.exception.UserNotFoundException;
import com.lp.service.YdlUserService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息表(YdlUser)表服务实现类
 *
 * @author makejava
 * @since 2022-10-07 18:08:42
 */
@Service("ydlUserService")
@Slf4j
public class YdlUserServiceImpl implements YdlUserService {
    @Resource
    private YdlUserDao ydlUserDao;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private CustomObjectMapper customObjectMapper; //序列化和反序列化
    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public YdlUser queryById(Long userId) {
        return this.ydlUserDao.queryById(userId);
    }

    /**
     * 分页查询
     *
     * @param ydlUser 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<YdlUser> queryByPage(YdlUser ydlUser, PageRequest pageRequest) {
        long total = this.ydlUserDao.count(ydlUser);
        log.info("total="+total);
        PageImpl<YdlUser> ydlUsers = new PageImpl<>(this.ydlUserDao.queryAllByLimit(ydlUser, pageRequest), pageRequest, total);
        log.info(String.valueOf(ydlUsers));
        return new PageImpl<>(this.ydlUserDao.queryAllByLimit(ydlUser, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(YdlUser ydlUser) {
        //加密密码
        String pwd = DigestUtils.md5DigestAsHex(ydlUser.getPassword().getBytes());
        ydlUser.setPassword(pwd);
        int insertRes = 0;
        if(ydlUser.getUserName() != null && ydlUser.getNickName() != null){
            insertRes =  this.ydlUserDao.insert(ydlUser);
        }
        return insertRes;
    }

    /**
     * 修改数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    @Override
    public YdlUser update(YdlUser ydlUser) {
        this.ydlUserDao.update(ydlUser);
        return this.queryById(ydlUser.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long userId) {
        return this.ydlUserDao.deleteById(userId) > 0;
    }

    /**
     * 登陆
     * @param userName
     * @param password
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public YdlLoginUser login(String userName, String password) throws JsonProcessingException {
        //1、登陆使用用户名查询用户，是否有该用户
        YdlUser ydlUser = ydlUserDao.queryByUserName(userName);
        if (ydlUser == null) throw new UserNotFoundException("执行登陆操作：【"+userName+"】用户不存在");
            //2、比较密码，若密码不正确，登陆失败
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(ydlUser.getPassword())) {
            log.info("执行登陆操作：【" + userName + "】密码输入错误");
            throw new PasswordIncorrectException("执行登陆操作：【" + userName + "】密码输入错误");
        }
        //3、登陆成功
        //（1）使用UUID生成token
        String token = UUID.randomUUID().toString();
        //（2）封装成YdlLoginToken,保存Redis中
        //获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//        log.info(request.getHeader());
        //获取浏览器数据
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        //获取IP地址
        ResponseEntity<String> res = restTemplate.getForEntity("https://whois.pconline.com.cn/ipJson.jsp?ip="+request.getRemoteHost()+"&json=true", String.class);
        String body = res.getBody();
//        log.info("body:"+body);
//        序列化获取的IP地址
        Map<String,String> value = customObjectMapper.readValue(body, new TypeReference<>() {
        });
        log.info("value:"+value);
        //获取IP地址
        String localInfo = value.get("addr")+value.get("pro")+value.get("city")+value.get("region");
        log.info("local:"+localInfo);
        //因ydlloginuser中增加@Builder注解，使用创建者模式来封装
        YdlLoginUser ydlLoginUser = YdlLoginUser.builder().userId(ydlUser.getUserId())
                .token(token)
                .browser(userAgent.getBrowser().getName())
                .ipaddr(request.getRemoteAddr())
                .os(userAgent.getOperatingSystem().getName())
                .loginLocation(localInfo)
                .loginTime(new Date())
                .ydlUser(ydlUser).build();
//key进行处理  token:username:uuid
//        1、根据生成的key的前缀进行判断  key前缀token:username:
        String keyPrefix = Constants.TOKEN_PREFIX + userName +":";
        //        String prefix = Constants.TOKEN_PREFIX;
        // 2、查询token:username:前缀的数据
        Set<String> keys = redisTemplate.keys(keyPrefix + "*");
        keys.forEach(log::info);
//        3、删除token
        keys.forEach(key ->redisTemplate.remove(key));
//        新数据写入Redis中
        redisTemplate.setObject(keyPrefix+token,ydlLoginUser,Constants.TOKEN_TIME);
        return ydlLoginUser;
    }
//退出
    @Override
    public void logout() {
//        删除Redis中的token user
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//        log.info("request="+request);
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);
        Set<String> keys = redisTemplate.keys(Constants.TOKEN_PREFIX + "*" + token);
        keys.forEach(key ->redisTemplate.remove(key));//移除Redis中存储的token
//        String keyPrefix = Constants.TOKEN_PREFIX + userName +":";
        log.info("token="+token);
//        return redisTemplate.remove(Constants.TOKEN_PREFIX+token);
    }

    /**
     * 通过ID查询用户权限和角色信息
     * @return
     */
    public HashMap<String, List<String>> getInfo(){
//        获取当前登陆对象
        YdlLoginUser loginUser = getLoginUser();
//        查询当前用户权限和角色
        YdlUser ydlUser = ydlUserDao.getInfo(loginUser.getUserId());

        // 3、处理权限和角色的相关信息
        // (1) roles:token : [admin,xxx,yyy]   perms:token: [system:user:add,system:user:update]
        //使用Stream流：map获取对象某一列数据,每个stream，只能使用一次
//        Stream<YdlRole> stream = ydlUser.getYdlRoles().stream(); 错误
        //        YdlRole::getRoleTag  lambuda表达式
        List<String> roleTags = ydlUser.getYdlRoles().stream().map(YdlRole::getRoleTag).collect(Collectors.toList());
        log.info("roleTag:"+roleTags);
//        写入Redis
        redisTemplate.setObject(Constants.ROLE_PREFIX+loginUser.getToken(),roleTags,30*60L);
        // [{roleName:cc,roleTag:xxx,perms:[{id,'xxx',perm:'system'},{id,'xxx',perm:'system'}]},{}]
        // [[{id,'xxx',perm:'system'},{id,'xxx',perm:'system'}],[{id,'xxx',perm:'system'},{id,'xxx',perm:'system'}]]
        // ['system','system:user:add']处理权限
        List<String> perms = new ArrayList<>();
        ydlUser.getYdlRoles().stream().map(YdlRole::getYdlMenus).forEach(ydlMenu -> perms.addAll(ydlMenu.stream().map(YdlMenu::getPerms).collect(Collectors.toList())));
        redisTemplate.setObject(Constants.PERM_PREFIX+loginUser.getToken(),perms,Constants.TOKEN_TIME);
//        整合成map数据
        HashMap<String,List<String>> data = new HashMap<>();
        data.put("roles",roleTags);
        data.put("perms",perms);
        log.info(String.valueOf(data));
        return data;
    }
    // 获取当前登陆用户的方法
    private YdlLoginUser getLoginUser(){
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
    public static void main(String[] args) throws JsonProcessingException {
//        获取IP地址
        RestTemplate restTemplate = new RestTemplate();
        CustomObjectMapper customObjectMapper = new CustomObjectMapper();
        ResponseEntity<String> res = restTemplate.getForEntity("https://whois.pconline.com.cn/ipJson.jsp?ip=122.255.144.12&json=true", String.class);
        System.out.println(res);
        String body1 = res.getBody();
        log.info("body:"+body1);
//        序列化获取的IP地址
        Map<String,String> value = customObjectMapper.readValue(body1, new TypeReference<>() {
        });
        log.info("value:"+value);
        String localInfo = value.get("addr")+value.get("pro")+value.get("city")+value.get("region");
        log.info("local:"+localInfo);
    }
}
