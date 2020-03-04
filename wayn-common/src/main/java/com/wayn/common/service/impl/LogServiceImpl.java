package com.wayn.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.common.dao.LogDao;
import com.wayn.common.domain.OperLog;
import com.wayn.common.enums.Operator;
import com.wayn.common.service.LogService;
import com.wayn.common.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogDao, OperLog> implements LogService {
    private static Map<String, String> map;

    static {
        map = new HashMap<>();
        for (Operator operator : Operator.values()) {
            map.put(operator.getCode(), operator.getName());
        }
    }

    @Autowired
    private LogDao logDao;

    @Override
    public Page<OperLog> listPage(Page<OperLog> page, OperLog log) {
        QueryWrapper<OperLog> wrapper = ParameterUtil.get();
        wrapper.like("userName", log.getUserName());
        wrapper.like("moduleName", log.getModuleName());
        wrapper.like("ip", log.getIp());
        wrapper.eq(StringUtils.isNotEmpty(log.getOperation()), "operation", log.getOperation());
        wrapper.eq(log.getOperateStatus() != null, "operState", log.getOperateStatus());
        wrapper.eq(StringUtils.isNotEmpty(log.getOperation()), "operation", log.getOperation());
        Page<OperLog> logPage = logDao.selectPage(page, wrapper);
        for (OperLog record : logPage.getRecords()) {
            record.setOperation(map.get(record.getOperation()));
        }
        return logPage;
    }
}
