package com.xywy.oauth.utils;

/**
 * Created by DongJr on 2016/3/23.
 */
public class MsgForCode {

    /**
     * 10000(成功)  30000(未知错误)  31001(行为不存在)  31002(project字段不能为空)  31003(手机号码不正确)  31004(该手机当天发送超过最大数量)  31005(N秒以内禁止重复发送（N为配置值）)  31006(其他原因导致发送失败)  31008(验证不通过)
     *
     * @param code
     * @return
     */
    public static String getMsg(String code) {

        if ("31014".equals(code)) {
            return "禁止重复绑定手机号";
        } else if ("31004".equals(code)) {
            return "该手机当天发送超过最大数量";
        } else if ("31015".equals(code)) {
            return "该手机号已被其他帐号绑定";
        } else if ("31008".equals(code)) {
            return "验证码错误";
        } else if ("31005".equals(code)) {
            return "验证码发送太频繁，请稍后再试";
        } else if ("31006".equals(code)) {
            return "发送失败";
        } else {
            return "当前网络不可用,请检查你的网络设置";
        }
    }
}
