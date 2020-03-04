package com.wayn.common.domain;

import com.wayn.common.enums.OnlineStatus;
import lombok.Data;

import java.util.Date;

@Data
public class UserOnline {

    private String id;

    private String userId;

    private String username;

    private String deptName;

    /**
     * 用户主机地址
     */
    private String host;


    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;


    /**
     * 在线状态
     */
    private OnlineStatus status = OnlineStatus.ON_LINE;

    /**
     * session创建时间
     */
    private Date startTimestamp;
    /**
     * session最后访问时间
     */
    private Date lastAccessTime;

    /**
     * 超时时间
     */
    private Long timeout;

    /**
     * 备份的当前用户会话
     */
    private String onlineSession;
}
