package com.wayn.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Data
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 8100277763813227258L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父级菜单ID
     */
    private Long pid;

    /**
     * 连接地址
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private BigDecimal sort;

    /**
     * 类别，1表示目录，2表示菜单，3表示按钮
     */
    private Integer type;

    /**
     * 编码
     */
    private String code;

    /**
     * 资源名称（菜单对应权限）
     */
    private String resource;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<Menu>();
}
