package com.wayn.common.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户管理，配置该用户所拥有得角色选择框
 * @author wayn
 *
 */
@Data
public class RoleChecked implements Serializable {

	private static final long serialVersionUID = 4921874281517667471L;
	private String id;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色描述
	 */
	private String roleDesc;

	/**
	 * 状态,1-启用,-1禁用
	 */
	private Integer roleState;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * role多选框，是否选中标志
	 */
	private Boolean checked = false;

}
