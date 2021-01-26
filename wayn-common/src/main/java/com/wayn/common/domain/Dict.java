package com.wayn.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayn.common.base.BusinessEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author wayn
 * @since 2019-06-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict")
public class Dict extends BusinessEntity<Dict> {

    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名
     */
    private String name;

    /**
     * 数据值
     */
    private String value;

    /**
     * 0 启用  1 禁用
     */
    private Integer dictStatus;

    /**
     * 1 字典类型  2 类型对应值
     */
    private Integer type;

    /**
     * 排序（升序）
     */
    private BigDecimal sort;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 创建者
     */
    private String createBy;


    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标记 0 存在 2 删除
     */
    private String delFlag;
}
