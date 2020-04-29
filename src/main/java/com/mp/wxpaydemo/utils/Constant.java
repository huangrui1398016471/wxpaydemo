package com.mp.wxpaydemo.utils;

/**
 * @author huangrui
 */
public class Constant {

    public static final String DOMAIN = "https://i-test.com.cn";

    public static final String APP_ID = "wx31cfa980dbca0204";

    public static final String APP_KEY = "chengdusiruiqixingxichanyegongsi";

    public static final String MCH_ID = "1579474491";  //商户号

    public static final String URL_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String URL_NOTIFY = Constant.DOMAIN + "/wxpay/views/payInfo.jsp";

    public static final String TIME_FORMAT = "yyyyMMddHHmmss";

    public static final int TIME_EXPIRE = 2;  //单位是day
}
