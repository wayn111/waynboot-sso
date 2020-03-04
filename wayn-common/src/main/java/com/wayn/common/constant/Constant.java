package com.wayn.common.constant;

public class Constant {
    /**
     * 字符编码
     */
    public static final String UTF_ENCODING = "UTF-8";
    /**
     * 字符串表示true
     */
    public static final String TRUE = "true";
    /**
     * 操作状态，成功
     */
    public static final Integer OPERATE_SUCCESS = 0;
    /**
     * 操作状态，失败
     */
    public static final Integer OPERATE_FAIL = 1;
    /**
     * 系统环境变量，默认为dev
     */
    public static String ENV = "dev";


    /**
     * 缓存方式
     */
    public static String CACHE_TYPE_REDIS = "redis";

    /**
     * 当前页
     */
    public static String PAGE_NUMBER = "pageNumber";

    /**
     * 分页大小
     */
    public static String PAGE_SIZE = "pageSize";

    /**
     * 排序字段名
     */
    public static String SORT_NAME = "sortName";

    /**
     * 排序方式 asc或者desc
     */
    public static String SORT_ORDER = "sortOrder";
}
