package com.wayn.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.common.dao.DictDao;
import com.wayn.common.domain.Dict;
import com.wayn.common.domain.User;
import com.wayn.common.service.DictService;
import com.wayn.common.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author wayn
 * @since 2019-06-27
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictDao, Dict> implements DictService {

    @Autowired
    private DictDao dictDao;

    @Override
    public Page<Dict> listPage(Page<Dict> page, Dict dict) {
        QueryWrapper<Dict> wrapper = ParameterUtil.get();
        Integer type = dict.getType();
        if (type == 2 && StringUtils.isNotEmpty(dict.getDictType())) {
            wrapper.eq("dictType", dict.getDictType());
        }
        wrapper.eq("type", type);
        wrapper.eq("delFlag", "0");
        wrapper.like("name", dict.getName());
        return dictDao.selectPage(page, wrapper);
    }

    @Override
    public boolean exists(Dict dict) {
        //如果是修改字典数据，数据值未改变则通过校验
        if (dict.getId() != null) {
            String value = dict.getValue();
            if (dictDao.selectById(dict.getId()).equals(value)) {
                return false;
            }
        }
        int count = dictDao.selectCount(new QueryWrapper<Dict>()
                .eq("value", dict.getValue())
                .eq("type", dict.getType())
                .eq("dictType", dict.getDictType())
                .eq("delFlag", 0));
        return count > 0;
    }

    @CacheEvict(value = "dictCache", allEntries = true)
    @Override
    public boolean save(Dict dict) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        dict.setCreateBy(user.getUserName());
        dict.setCreateTime(new Date());
        return super.save(dict);
    }

    @CacheEvict(value = "dictCache", allEntries = true)
    @Override
    public boolean update(Dict dict) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        dict.setUpdateBy(user.getUserName());
        dict.setUpdateTime(new Date());
        return updateById(dict);
    }

    @CacheEvict(value = "dictCache", allEntries = true)
    @Override
    public boolean remove(Serializable id) {
        return update(new UpdateWrapper<Dict>().eq("id", id).set("delFlag", "2"));
    }

    @CacheEvict(value = "dictCache", allEntries = true)
    @Override
    public boolean batchRemove(Serializable[] ids) {
        return update(new UpdateWrapper<Dict>().in("id", Arrays.asList(ids)).set("delFlag", "2"));
    }

    @Cacheable(value = "dictCache", key = "#root.method + '_' + #root.args[0]")
    @Override
    public List<JSONObject> selectDicts(String dictTypeSelected) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 1);
        wrapper.eq("delFlag", "0");
        List<Dict> dicts = dictDao.selectList(wrapper);
        List<JSONObject> objectList = dicts.stream().map(data -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", data.getValue());
            jsonObject.put("text", data.getName());
            if (dictTypeSelected.equals(data.getValue())) {
                jsonObject.put("selected", true);
            }
            return jsonObject;
        }).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "");
        jsonObject.put("text", "全部");
        objectList.add(0, jsonObject);
        return objectList;
    }

    @Cacheable(value = "dictCache", key = "#root.method + '_' + #root.args[0]")
    @Override
    public List<JSONObject> selectDictsValueByType(String dictType) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 2)
                .eq("delFlag", "0")
                .eq("dictType", dictType)
                .eq("dictStatus", 1);
        List<Dict> dictList = dictDao.selectList(wrapper);
        List<JSONObject> objectList = convert2select(dictList);
        return objectList;
    }

    /**
     * 将字典列表转化为select2接受的json格式
     *
     * @param dicts
     * @return
     */
    public List<JSONObject> convert2select(List<Dict> dicts) {
        return dicts.stream().map(data -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", data.getValue());
            jsonObject.put("text", data.getName());
            return jsonObject;
        }).collect(Collectors.toList());
    }
}
