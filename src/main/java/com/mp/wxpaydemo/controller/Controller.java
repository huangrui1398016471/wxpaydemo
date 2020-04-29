package com.mp.wxpaydemo.controller;


import com.mp.wxpaydemo.pojo.PayInfo;
import com.mp.wxpaydemo.utils.CommonUtil;
import com.mp.wxpaydemo.utils.Constant;
import com.mp.wxpaydemo.utils.HttpUtil;
import com.mp.wxpaydemo.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author huangrui
 */
@RestController
@RequestMapping("/wxpay")
public class Controller {
    //  小程序信息  不能是测试号
    private String appid = "wx31cfa980dbca0204";
    private String openId = "obBJP5agbqQH6m7IKynR5YClegHs";

    /**
     * 功能描述: <br>
     * @Param: []
     * @Return: java.lang.String
     * @author huangrui
     * @Date: 2020/1/22 14:45
     */
    @RequestMapping("/weixinlogin")
    public Map<String, String> weixinlogin(HttpServletRequest request) {
        System.out.println("开始");
        String clientIP = CommonUtil.getClientIp(request);
        String nonceStr = "chengdusiruiqixingxichanyegongsi";
        Map<String, String> stringStringMap = unifiedOrder(clientIP, nonceStr);
        System.out.println(stringStringMap);
        System.out.println("结束");
        return stringStringMap;

    }
    @PostMapping("/secondSign")
    public String secondSign(@RequestBody Map<String,String> map){
        System.out.println("map:"+map);
//        开始签名
        System.out.println("签名开始");
        StringBuffer sb = new StringBuffer();
        sb.append("appId=" + appid)
                .append("&nonceStr=" +  map.get("nonceStr"))
                .append("&package=" + map.get("package"))
                .append("&signType=" + map.get("signType"))
                .append("&timeStamp=" + map.get("timeStamp"))
                .append("&key=" + Constant.APP_KEY);
        System.out.println("拼接字符串："+sb);
        String sign = sb.toString().trim();
        try {
            String s = CommonUtil.MD5(sign);
            System.out.println("签名成功："+s);
            return s;
        } catch (Exception e) {
            System.out.println("签名失败");
            return "";
        }
    }




    /**
     * 调用统一下单接口
     */
    private Map<String, String> unifiedOrder(String clientIP, String randomNonceStr) {
        System.out.println("--------1---------------");
        try {
            System.out.println("--------2---------------");
            String url = Constant.URL_UNIFIED_ORDER;

            PayInfo payInfo = createPayInfo(openId, clientIP, randomNonceStr);
            String md5 = getSign(payInfo);
            payInfo.setSign(md5);

            System.out.println("payINfo"+payInfo);
            String xml = CommonUtil.payInfoToXML(payInfo);
            xml = xml.replace("__", "_").replace("<![CDATA[1]]>", "1");
            //xml = xml.replace("__", "_").replace("<![CDATA[", "").replace("]]>", "");

            StringBuffer buffer = HttpUtil.httpsRequest(url, "POST", xml);
            Map<String, String> result = CommonUtil.parseXml(buffer.toString());


            String return_code = result.get("return_code");
            if(StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")) {
                System.out.println(result);
                String return_msg = result.get("return_msg");
                if(StringUtils.isNotBlank(return_msg) && !return_msg.equals("OK")) {
                    //log.error("统一下单错误！");
                    return null;
                }

                String prepay_Id = result.get("prepay_id");
                return result;

            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("--------error---------------");
            e.printStackTrace();
        }

        return null;
    }








    private String getSign(PayInfo payInfo) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("appid=" + payInfo.getAppid())
                .append("&attach=" + payInfo.getAttach())
                .append("&body=" + payInfo.getBody())
                .append("&device_info=" + payInfo.getDevice_info())
                .append("&limit_pay=" + payInfo.getLimit_pay())
                .append("&mch_id=" + payInfo.getMch_id())
                .append("&nonce_str=" + payInfo.getNonce_str())
                .append("&notify_url=" + payInfo.getNotify_url())
                .append("&openid=" + payInfo.getOpenid())
                .append("&out_trade_no=" + payInfo.getOut_trade_no())
                .append("&sign_type=" + payInfo.getSign_type())
                .append("&spbill_create_ip=" + payInfo.getSpbill_create_ip())
                .append("&time_expire=" + payInfo.getTime_expire())
                .append("&time_start=" + payInfo.getTime_start())
                .append("&total_fee=" + payInfo.getTotal_fee())
                .append("&trade_type=" + payInfo.getTrade_type())
                .append("&key=" + Constant.APP_KEY);
                String sign = sb.toString().trim();
        String s = CommonUtil.getMD5(sign).toUpperCase();
        System.out.println(s);
        return s;
    }


    private PayInfo createPayInfo(String openId, String clientIP, String randomNonceStr) {

        Date date = new Date();
        String timeStart = TimeUtils.getFormatTime(date, Constant.TIME_FORMAT);
        String timeExpire = TimeUtils.getFormatTime(TimeUtils.addDay(date, Constant.TIME_EXPIRE), Constant.TIME_FORMAT);

        String randomOrderId = CommonUtil.getRandomOrderId();

        PayInfo payInfo = new PayInfo();
        payInfo.setAppid(Constant.APP_ID);
        payInfo.setMch_id(Constant.MCH_ID);
        payInfo.setDevice_info("WEB");
        payInfo.setNonce_str(randomNonceStr);
        payInfo.setSign_type("MD5");  //默认即为MD5
        payInfo.setBody("JSAPI支付测试");
        payInfo.setAttach("支付测试4luluteam");
        payInfo.setOut_trade_no(randomOrderId);
        payInfo.setTotal_fee(1);
        payInfo.setSpbill_create_ip(clientIP);
        payInfo.setTime_start(timeStart);
        payInfo.setTime_expire(timeExpire);
        payInfo.setNotify_url(Constant.URL_NOTIFY);
        payInfo.setTrade_type("JSAPI");
        payInfo.setLimit_pay("no_credit");
        payInfo.setOpenid(openId);

        return payInfo;
    }



}
