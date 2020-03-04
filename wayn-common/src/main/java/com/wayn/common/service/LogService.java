package com.wayn.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wayn.common.domain.OperLog;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
public interface LogService extends IService<OperLog> {
    Page<OperLog> listPage(Page<OperLog> page, OperLog log);
}
