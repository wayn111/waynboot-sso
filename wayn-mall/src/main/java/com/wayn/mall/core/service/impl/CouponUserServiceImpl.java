package com.wayn.mall.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.mall.core.dao.CouponDao;
import com.wayn.mall.core.dao.CouponUserDao;
import com.wayn.mall.core.entity.Coupon;
import com.wayn.mall.core.entity.CouponUser;
import com.wayn.mall.core.service.CouponService;
import com.wayn.mall.core.service.CouponUserService;
import com.wayn.mall.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CouponUserServiceImpl extends ServiceImpl<CouponUserDao, CouponUser> implements CouponUserService {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponDao couponDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCouponUser(Long couponId, Long userId) {
        Coupon coupon = couponService.getById(couponId);
        if (coupon.getCouponLimit() != 0) {
            int count = count(new QueryWrapper<CouponUser>()
                    .eq("user_id", userId)
                    .eq("coupon_id", coupon.getCouponId()));
            if (count != 0) {
                throw new BusinessException("优惠卷已经领过了,无法再次领取！");
            }
        }
        if (coupon.getCouponTotal() != 0) {
            int count = count(new QueryWrapper<CouponUser>()
                    .eq("coupon_id", coupon.getCouponId()));
            if (count >= coupon.getCouponTotal()) {
                throw new BusinessException("优惠卷已经领完了！");
            }
            if (couponDao.reduceCouponTotal(couponId) <= 0) {
                throw new BusinessException("优惠卷领取失败！");
            }
        }
        CouponUser couponUser = new CouponUser();
        couponUser.setUserId(userId);
        couponUser.setCouponId(couponId);
        LocalDate startLocalDate = LocalDate.now();
        LocalDate endLocalDate = startLocalDate.plusDays(coupon.getDays());
        ZoneId zone = ZoneId.systemDefault();
        Date startDate = Date.from(startLocalDate.atStartOfDay().atZone(zone).toInstant());
        Date endDate = Date.from(endLocalDate.atStartOfDay().atZone(zone).toInstant());
        couponUser.setStartTime(startDate);
        couponUser.setEndTime(endDate);
        couponUser.setCreateTime(new Date());
        return save(couponUser);
    }

    @Override
    public Coupon getCoupon(Long orderId) {
        CouponUser couponUser = getOne(new QueryWrapper<CouponUser>().eq("order_id", orderId));
        if (couponUser == null) {
            return null;
        }
        return couponService.getById(couponUser.getCouponId());
    }
}
