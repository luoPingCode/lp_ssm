package com.lp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * 角色和菜单关联表(YdlRoleMenu)实体类
 *
 * @author makejava
 * @since 2022-10-07 18:08:40
 */
//分页使用的
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
public class YdlBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private int page;

    private int size;

    private Sort sort;

}

