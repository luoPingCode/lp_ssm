package com.lp.entity;

import java.io.Serializable;

/**
 * 用户和角色关联表(YdlUserRole)实体类
 *
 * @author makejava
 * @since 2022-10-07 18:08:43
 */
public class YdlUserRole implements Serializable {
    private static final long serialVersionUID = -93630817094052705L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}

