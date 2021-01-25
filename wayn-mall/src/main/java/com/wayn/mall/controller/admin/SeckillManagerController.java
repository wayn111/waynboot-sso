package com.wayn.mall.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.constant.Constants;
import com.wayn.mall.controller.base.BaseController;
import com.wayn.mall.core.entity.Seckill;
import com.wayn.mall.core.entity.vo.SeckillVO;
import com.wayn.mall.core.service.SeckillService;
import com.wayn.mall.redis.RedisCache;
import com.wayn.mall.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("admin/seckill")
public class SeckillManagerController extends BaseController {

    private static final String PREFIX = "admin/seckill";

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisCache redisCache;

    @GetMapping
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "seckill");
        return PREFIX + "/seckill";
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResponseBody
    public IPage list(SeckillVO seckillVO, HttpServletRequest request) {
        Page<Seckill> page = getPage(request);
        return seckillService.selectPage(page, seckillVO);
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(@RequestBody Seckill seckill) {
        boolean save = seckillService.save(seckill);
        if (save) {
            // 库存预热
            redisCache.setCacheObject(Constants.SECKILL_GOODS_STOCK_KEY + seckill.getSeckillId(), seckill.getSeckillNum());
        }
        return R.result(save);
    }

    @ResponseBody
    @PostMapping("/update")
    public R update(@RequestBody Seckill seckill) {
        seckill.setUpdateTime(new Date());
        boolean update = seckillService.updateById(seckill);
        if (update) {
            // 库存预热
            redisCache.setCacheObject(Constants.SECKILL_GOODS_STOCK_KEY + seckill.getSeckillId(), seckill.getSeckillNum());
        }
        return R.result(update);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    @ResponseBody
    public R Info(@PathVariable("id") Long id) {
        return R.success().add("data", seckillService.getById(id));
    }
}
