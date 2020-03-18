package com.wayn.mall.controller.mall;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wayn.mall.constant.Constants;
import com.wayn.mall.controller.vo.GoodsCategoryVO;
import com.wayn.mall.entity.Carousels;
import com.wayn.mall.entity.Goods;
import com.wayn.mall.enums.IndexConfigTypeEnum;
import com.wayn.mall.service.CarouselsService;
import com.wayn.mall.service.GoodsCategoryService;
import com.wayn.mall.service.GoodsService;
import com.wayn.mall.service.IndexConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MallController {

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CarouselsService carouselsService;

    @Autowired
    private IndexConfigService indexConfigService;

    @GetMapping("index")
    public String index(HttpServletRequest request) {
        List<GoodsCategoryVO> root = goodsCategoryService.treeList();
        List<Goods> hotGoodses = indexConfigService.listIndexConfig(IndexConfigTypeEnum.INDEX_GOODS_HOT, Constants.INDEX_GOODS_HOT_NUMBER);
        List<Goods> newGoodses = indexConfigService.listIndexConfig(IndexConfigTypeEnum.INDEX_GOODS_NEW, Constants.INDEX_GOODS_NEW_NUMBER);
        List<Goods> recommendGoodses = indexConfigService.listIndexConfig(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND, Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        List<Carousels> carousels = carouselsService.list(new QueryWrapper<Carousels>()
                .eq("is_deleted", 0)
                .orderByDesc("carousel_rank"));
        request.setAttribute("categories", root);// 分类数据
        request.setAttribute("carousels", carousels);// 轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
}
