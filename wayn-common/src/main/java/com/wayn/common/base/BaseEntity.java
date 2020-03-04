package com.wayn.common.base;


import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 6746161463502783836L;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    private Date createTime;

    public String getStartTime() {
        return startTime;
    }

    public BaseEntity<T> setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public BaseEntity<T> setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public BaseEntity<T> setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("startTime", startTime)
                .append("endTime", endTime)
                .append("createTime", createTime)
                .toString();
    }
}
