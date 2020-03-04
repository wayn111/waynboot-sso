package com.wayn.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayn.common.base.BusinessEntity;
import lombok.Data;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Data
@TableName("sys_role")
public class Role extends BusinessEntity<Role> {


    /**
     * 主键
     */
    @TableId(type=IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 状态 0启用 1禁用
     */
    private Integer roleStatus;
}
