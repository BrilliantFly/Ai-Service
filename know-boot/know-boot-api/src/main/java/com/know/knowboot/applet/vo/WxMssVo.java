package com.know.knowboot.applet.vo;

import lombok.Data;

import java.util.Map;

@Data
public class WxMssVo {

    private String touser;//用户openid
    private String template_id;//模版id
    private String page = "index";//默认跳到小程序首页
    private String form_id;//收集到的用户formid
    private String miniprogram_state = "developer";
    private String lang = "zh_CN";
    private Map<String, TemplateDataVo> data;//推送文字

}
