package com.wayn.mall.controller.mall;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wayn.mall.constant.Constants;
import com.wayn.mall.controller.base.BaseController;
import com.wayn.mall.core.entity.ShopCat;
import com.wayn.mall.core.entity.vo.MallUserVO;
import com.wayn.mall.core.entity.vo.MyCouponVO;
import com.wayn.mall.core.entity.vo.ShopCatVO;
import com.wayn.mall.core.service.CouponService;
import com.wayn.mall.core.service.ShopCatService;
import com.wayn.mall.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MallShopCatController extends BaseController {

    @Autowired
    private ShopCatService shopCatService;

    @Autowired
    private CouponService couponService;

    @ResponseBody
    @RequestMapping(value = "shopCart", method = {RequestMethod.POST, RequestMethod.PUT})
    public R save(@RequestBody ShopCat shopCat, HttpSession session) {
        shopCat.setUserId(((MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY)).getUserId());
        shopCatService.saveShopCat(shopCat);
        return R.success();
    }

    @ResponseBody
    @DeleteMapping("shopCart/{id}")
    public R delete(@PathVariable("id") Long shopCatId) {
        shopCatService.removeById(shopCatId);
        return R.success();
    }


    @GetMapping("shopCart")
    public String shopCart(HttpServletRequest request, HttpSession session) {
        MallUserVO mallUserVO = (MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<ShopCatVO> collect = shopCatService.getShopCatVOList(mallUserVO.getUserId());
        if (CollectionUtils.isNotEmpty(collect)) {
            itemsTotal = collect.size();
            for (ShopCatVO shopCatVO : collect) {
                priceTotal += shopCatVO.getGoodsCount() * shopCatVO.getSellingPrice();
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", collect);
        return "mall/shop-cat";
    }

    @GetMapping("shopCart/settle")
    public String settle(HttpServletRequest request, HttpSession session) {
        MallUserVO mallUserVO = (MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<ShopCat> cats = shopCatService.list(new QueryWrapper<ShopCat>().eq("user_id", mallUserVO.getUserId()));
        if (CollectionUtils.isEmpty(cats)) {
            return "shop-cart";
        }
        int priceTotal = 0;
        List<ShopCatVO> collect = shopCatService.getShopCatVOList(mallUserVO.getUserId());
        if (CollectionUtils.isNotEmpty(collect)) {
            for (ShopCatVO shopCatVO : collect) {
                priceTotal += shopCatVO.getGoodsCount() * shopCatVO.getSellingPrice();
            }
        }
        List<MyCouponVO> myCouponVOS = couponService.selectMyCoupons(collect, priceTotal, mallUserVO.getUserId());
        request.setAttribute("coupons", myCouponVOS);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", collect);
        return "mall/order-settle";
    }

}
