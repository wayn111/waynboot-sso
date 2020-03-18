package com.wayn.mall.controller.mall;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wayn.mall.base.BaseController;
import com.wayn.mall.constant.Constants;
import com.wayn.mall.controller.vo.MallUserVO;
import com.wayn.mall.controller.vo.ShopCatVO;
import com.wayn.mall.entity.ShopCat;
import com.wayn.mall.service.ShopCatService;
import com.wayn.mall.util.R;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShopCatController extends BaseController {

    @Autowired
    private ShopCatService shopCatService;

    @ResponseBody
    @RequestMapping(value = "shop-cart", method = {RequestMethod.POST, RequestMethod.PUT})
    public R save(@RequestBody ShopCat shopCat, HttpSession session) {
        shopCat.setUserId(((MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY)).getUserId());
        shopCatService.saveShopCat(shopCat);
        return R.success();
    }

    @ResponseBody
    @DeleteMapping("shop-cart/{id}")
    public R delete(@PathVariable("id") Long shopCatId) {
        shopCatService.removeById(shopCatId);
        return R.success();
    }


    @GetMapping("shop-cart")
    public String save(HttpServletRequest request, HttpSession session) {
        MallUserVO mallUserVO = (MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Long itemsTotal = 0L;
        double priceTotal = 0;
        List<ShopCatVO> collect = shopCatService.getShopcatVOList(mallUserVO.getUserId());
        if (CollectionUtils.isNotEmpty(collect)) {
            itemsTotal = collect.stream().count();
            for (ShopCatVO shopCatVO : collect) {
                priceTotal += shopCatVO.getGoodsCount() * shopCatVO.getSellingPrice();
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", collect);
        return "mall/cat";
    }

    @GetMapping("shop-cart/settle")
    public String delete(HttpServletRequest request, HttpSession session) {
        MallUserVO mallUserVO = (MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<ShopCat> cats = shopCatService.list(new QueryWrapper<ShopCat>().eq("user_id", mallUserVO.getUserId()));
        if (CollectionUtils.isEmpty(cats)) {
            return "shop-cart";
        }
        double priceTotal = 0;
        List<ShopCatVO> collect = shopCatService.getShopcatVOList(mallUserVO.getUserId());
        if (CollectionUtils.isNotEmpty(collect)) {
            for (ShopCatVO shopCatVO : collect) {
                priceTotal += shopCatVO.getGoodsCount() * shopCatVO.getSellingPrice();
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", collect);
        return "mall/order-settle";
    }

}
