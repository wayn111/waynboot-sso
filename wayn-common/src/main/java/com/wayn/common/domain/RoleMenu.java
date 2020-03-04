package com.wayn.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单关联表
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Data
@TableName("sys_role_menu")
public class RoleMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 角色主键
	 */
	private String roleId;

	/**
	 * 菜单主键
	 */
	private Long menuId;
}
