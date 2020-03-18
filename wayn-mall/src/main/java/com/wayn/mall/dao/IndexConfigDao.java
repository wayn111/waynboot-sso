package com.wayn.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.entity.IndexConfig;

public interface IndexConfigDao extends BaseMapper<IndexConfig> {

    IPage selectListPage(Page page, IndexConfig indexConfig);
}
