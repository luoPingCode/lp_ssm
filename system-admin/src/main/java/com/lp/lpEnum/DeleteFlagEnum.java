package com.lp.lpEnum;

/**
 * @author LuoPing
 * @date 2022/10/17 20:00
 * 用户表中del_flag 字段枚举
 */
public enum  DeleteFlagEnum {
    //yes代表逻辑上删除，no代表逻辑上没有删除
    YSE("1"),NO("0");

    private String value;

    DeleteFlagEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
