package com.wayn.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayn.common.base.BaseEntity;
import lombok.Data;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Data
@TableName("sys_oper_log")
public class OperLog extends BaseEntity<OperLog> {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 操作状态 0 正常 1 失败
     */
    private Integer operateStatus;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 用户
     */
    private String userName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作类型
     */
    private String operation;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * 浏览器信息
     */
    private String agent;


    /**
     * 执行时间
     */
    private Long executeTime;

    /**
     * 参数
     */
    private String requestParams;

    /**
     * 请求类型
     */
    private String requestMethod;

    /**
     * 请求响应
     */
    private String requestResponse;
}
