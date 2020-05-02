package com.wayn.mall.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.base.BaseController;
import com.wayn.mall.constant.Constants;
import com.wayn.mall.entity.GoodsCategory;
import com.wayn.mall.service.GoodsCategoryService;
import com.wayn.mall.util.R;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/categories")
public class GoodsCategoryController extends BaseController {

    private static final String PREFIX = "admin/category";

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @GetMapping
    public String index(GoodsCategory goodsCategory, String backParentId, HttpServletRequest request) {
        request.setAttribute("path", "newbee_mall_category");
        request.setAttribute("categoryLevel", goodsCategory.getCategoryLevel());
        request.setAttribute("parentId", goodsCategory.getParentId());
        request.setAttribute("backParentId", backParentId);
        return PREFIX + "/category";
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResponseBody
    public IPage list(GoodsCategory goodsCategory, HttpServletRequest request) {
        Page<GoodsCategory> page = getPage(request);
        return goodsCategoryService.selectPage(page, goodsCategory);
    }

    /**
     * 列表
     */
    @GetMapping(value = "/listForSelect")
    @ResponseBody
    public R listForSelect(@RequestParam("categoryId") Long categoryId) {
        GoodsCategory category = goodsCategoryService.getById(categoryId);
        // 既不是一级分类也不是二级分类则为不返回数据
        if (category == null || category.getCategoryLevel() == Constants.CATEGORY_LEVEL_THREE) {
            return R.error("参数异常！");
        }
        Map categoryResult = new HashMap(2);
        if (category.getCategoryLevel() == Constants.CATEGORY_LEVEL_ONE) {
            //如果是一级分类则返回当前一级分类下的所有二级分类，以及二级分类列表中第一条数据下的所有三级分类列表
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = goodsCategoryService.list(new QueryWrapper<GoodsCategory>()
                    .eq("category_level", Constants.CATEGORY_LEVEL_TWO).eq("parent_id", categoryId));
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = goodsCategoryService.list(new QueryWrapper<GoodsCategory>()
                        .eq("category_level", Constants.CATEGORY_LEVEL_THREE).eq("parent_id", secondLevelCategories.get(0).getCategoryId()));
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if (category.getCategoryLevel().equals(Constants.CATEGORY_LEVEL_TWO)) {
            //如果是二级分类则返回当前分类下的所有三级分类列表
            List<GoodsCategory> thirdLevelCategories = goodsCategoryService.list(new QueryWrapper<GoodsCategory>()
                    .eq("category_level", Constants.CATEGORY_LEVEL_THREE).eq("parent_id", categoryId));
            categoryResult.put("thirdLevelCategories", thirdLevelCategories);
        }
        return R.success().add("data", categoryResult);
    }

    /**
     * 列表
     */
    @PostMapping("/save")
    @ResponseBody
    public R save(@RequestBody GoodsCategory goodsCategory) {
        goodsCategoryService.save(goodsCategory);
        return R.success();
    }

    /**
     * 列表
     */
    @PostMapping("/update")
    @ResponseBody
    public R update(@RequestBody GoodsCategory goodsCategory) {
        goodsCategoryService.updateById(goodsCategory);
        return R.success();
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    @ResponseBody
    public R Info(@PathVariable("id") Integer id) {
        return R.success().add("data", goodsCategoryService.getById(id));
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ResponseBody
    public R delete(@RequestBody List<Long> ids) {
        // 如果是一级分类，则添加一级分类
        List<Long> list = new ArrayList<>(ids);
        for (Long id : ids) {
            // 查询二级分类
            List<GoodsCategory> secondCates = goodsCategoryService.list(new QueryWrapper<GoodsCategory>().select("category_id").eq("parent_id", id));
            if (CollectionUtils.isNotEmpty(secondCates)) {
                List<Long> secondCatesIds = secondCates.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
                list.addAll(secondCatesIds);
                for (Long aLong : secondCatesIds) {
                    // 查询三级分类
                    List<GoodsCategory> thirdCates = goodsCategoryService.list(new QueryWrapper<GoodsCategory>().select("category_id").eq("parent_id", aLong));
                    List<Long> thirdCatesIds = thirdCates.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
                    list.addAll(thirdCatesIds);
                }
            }
        }
        goodsCategoryService.removeByIds(list);
        return R.success();
    }
}
