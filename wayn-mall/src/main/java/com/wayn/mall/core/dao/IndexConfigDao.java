package com.wayn.mall.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.core.entity.IndexConfig;

public interface IndexConfigDao extends BaseMapper<IndexConfig> {

    IPage<IndexConfig> selectListPage(Page page, IndexConfig indexConfig);
}
