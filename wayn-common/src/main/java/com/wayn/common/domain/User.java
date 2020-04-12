package com.wayn.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayn.common.base.BusinessEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@ApiModel(value = "用户表")
@Data
@TableName("sys_user")
public class User extends BusinessEntity<User> {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户状态 0启用 1禁用
     */
    private Integer userStatus;

    /**
     * 头像
     */
    private String userImg;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门主键
     */
    private Long deptId;
}
