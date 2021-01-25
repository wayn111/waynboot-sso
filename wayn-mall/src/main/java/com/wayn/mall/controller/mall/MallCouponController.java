package com.wayn.mall.controller.mall;


import com.wayn.mall.constant.Constants;
import com.wayn.mall.core.entity.vo.CouponVO;
import com.wayn.mall.core.entity.vo.MallUserVO;
import com.wayn.mall.core.service.CouponService;
import com.wayn.mall.core.service.CouponUserService;
import com.wayn.mall.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MallCouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponUserService couponUserService;

    @GetMapping("coupon")
    public String index(HttpServletRequest request, HttpSession session) {
        Long userId = null;
        if (session.getAttribute(Constants.MALL_USER_SESSION_KEY) != null) {
            userId = ((MallUserVO) request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)).getUserId();
        }
        List<CouponVO> coupons = couponService.selectAvailableCoupon(userId);
        request.setAttribute("coupons", coupons);
        return "com.wayn.mall/coupon";
    }

    @ResponseBody
    @PostMapping("coupon/{couponId}")
    public R save(@PathVariable Long couponId, HttpSession session) {
        MallUserVO mallUserVO = (MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        couponUserService.saveCouponUser(couponId, mallUserVO.getUserId());
        return R.success();
    }
}
