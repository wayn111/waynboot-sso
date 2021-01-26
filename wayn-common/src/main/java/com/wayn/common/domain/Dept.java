package com.wayn.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayn.common.base.BusinessEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dept")
public class Dept extends BusinessEntity<Dept> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	private Long pid;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 排序
	 */
	private BigDecimal sort;

}
