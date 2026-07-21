package com.know.knowboot.applet.service;

import com.alibaba.fastjson.JSONObject;
import com.know.knowboot.applet.vo.WxMssVo;
import com.know.knowboot.base.Result;

/**
 * 小程序接口对接
 */
public interface AppletApiService {

    /**
     * 用户登录
     *
     * @param jsCode    登录时获取的 code
     * @return
     */
    public Result<JSONObject> userLogin(String jsCode);


    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）。
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     *
     * @return
     */
    public Result<JSONObject> getAccessToken();

    /**
     * code换取用户手机号
     * @param accessToken
     * @param code
     * @return
     */
    public Result<JSONObject> getPhoneNumber(String accessToken, String code);

    /**
     * 订阅消息 - 发送
     * @param accessToken
     * @param wxMssVo
     * @return
     */
    public Result<JSONObject> sendNewTemplate(String accessToken, WxMssVo wxMssVo);

}
