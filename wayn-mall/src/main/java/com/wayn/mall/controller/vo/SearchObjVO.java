package com.wayn.mall.controller.vo;

import lombok.Data;

/**
 * 查询参数封装对象
 */
@Data
public class SearchObjVO {

    String keyword;

    Long goodsCategoryId;
}
