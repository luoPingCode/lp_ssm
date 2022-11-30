package com.lp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lp.entity.YdlLoginUser;
import com.lp.entity.YdlUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;

/**
 * 用户信息表(YdlUser)表服务接口
 *
 * @author makejava
 * @since 2022-10-07 18:08:42
 */
public interface YdlUserService {

    /**
     * 通过id查询用户角色和权限
     * @return
     */
    HashMap<String, List<String>> getInfo();
    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    YdlUser queryById(Long userId);

    /**
     * 分页查询
     *
     * @param ydlUser 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<YdlUser> queryByPage(YdlUser ydlUser, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    int insert(YdlUser ydlUser);

    /**
     * 修改数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    YdlUser update(YdlUser ydlUser);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Long userId);

    /**
     * 登陆
     */
    YdlLoginUser login(String userName, String password) throws JsonProcessingException;

    /**
     * 退出
     */
    void logout();


}
