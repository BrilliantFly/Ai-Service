package com.know.knowboot.applet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.know.knowboot.applet.enums.AppletParamEnum;
import com.know.knowboot.applet.properties.AppletProperties;
import com.know.knowboot.applet.service.AppletApiService;
import com.know.knowboot.applet.vo.WxMssVo;
import com.know.knowboot.base.Result;
import com.know.knowboot.http.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 小程序接口对接
 */
@Slf4j
@Service
public class AppletApiServiceImpl implements AppletApiService {

    @Autowired
    private HttpClientUtils httpClientUtils;

    @Autowired
    private AppletProperties appletProperties;

    @Autowired
    Environment env;

    /**
     * 用户登录
     *
     * @param jsCode 登录时获取的 code
     * @return
     */
    @Override
    public Result<JSONObject> userLogin(String jsCode) {

        JSONObject object = httpClientUtils.getEntityByParam(appletProperties.getLOGIN_URL(), new HashMap() {{
            put(AppletParamEnum.KEY_APPID_ID.getCode(), appletProperties.getAppId());
            put(AppletParamEnum.KEY_SECRET.getCode(), appletProperties.getAppSecert());
            put(AppletParamEnum.KEY_JS_CODE.getCode(), jsCode);
            put(AppletParamEnum.KEY_GRANT_TYPE.getCode(), AppletParamEnum.VALUE_AUTHORIZATION_CODE_LOGIN.getCode());
        }});
        // 请求成功
        if (!object.containsKey(AppletParamEnum.KEY_ERRCODE.getCode())) {
            log.info("------------------- 小程序Api对接 - 登录成功 ----------------------------");
            return Result.ok();
        } else {
            if (object.getString(AppletParamEnum.KEY_ERRMSG.getCode()).equals(AppletParamEnum.KEY_OK.getCode())) {
                log.info("------------------- 小程序Api对接 - 登录成功 ---------------------------- {}", object);
                return Result.ok();
            } else {
                log.info("------------------- 小程序Api对接 ---------------------------- 登录失败 - 错误信息 -> {}", object.getString(AppletParamEnum.KEY_ERRMSG.getCode()));
                return Result.error("登录失败");
            }
        }
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）。
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     *
     * @return
     */
    @Override
    public Result<JSONObject> getAccessToken() {

        JSONObject object = httpClientUtils.getEntityByParam(appletProperties.getGET_ACCESS_TOKE(), new HashMap() {{
            put(AppletParamEnum.KEY_APPID_ID.getCode(), appletProperties.getAppId());
            put(AppletParamEnum.KEY_SECRET.getCode(), appletProperties.getAppSecert());
            put(AppletParamEnum.KEY_GRANT_TYPE.getCode(), AppletParamEnum.VALUE_AUTHORIZATION_CODE_ACCESS_TOKEN.getCode());
        }});

        // 请求成功
        if (!object.containsKey(AppletParamEnum.KEY_ERRCODE.getCode())) {
            log.info("------------------- 小程序Api对接 - 获取小程序全局唯一后台接口调用凭据成功 ----------------------------");
            return Result.ok();
        } else {
            if (object.getString(AppletParamEnum.KEY_ERRMSG.getCode()).equals(AppletParamEnum.KEY_OK.getCode())) {
                log.info("------------------- 小程序Api对接 - 获取小程序全局唯一后台接口调用凭据成功 ---------------------------- {}", object);
                return Result.ok();
            } else {
                log.info("------------------- 小程序Api对接 ---------------------------- 获取小程序全局唯一后台接口调用凭据失败 - 错误信息 -> {}", object.getString(AppletParamEnum.KEY_ERRMSG.getCode()));
                return Result.error(" 获取小程序全局唯一后台接口调用凭据失败");
            }
        }
    }

    /**
     * code换取用户手机号
     *
     * @param accessToken 接口调用凭证
     * @param code        手机号获取凭证
     * @return
     */
    @Override
    public Result<JSONObject> getPhoneNumber(String accessToken, String code) {

        String requestUrl = appletProperties.getGET_PHONE_NUMBER() + "?" + AppletParamEnum.KEY_ACCESS_TOKEN.getCode() + "=" + accessToken;

//        JSONObject object = httpClientUtils.postEntityByParam(requestUrl,headersParam,new HashMap() {{
//            put(AppletParamEnum.KEY_CODE.getCode(), code);
//        }});

        JSONObject object = httpClientUtils.postEntityByParam(requestUrl, new HashMap() {{
            put(AppletParamEnum.KEY_CODE.getCode(), code);
        }});

        // 请求成功
        if (!object.containsKey(AppletParamEnum.KEY_ERRCODE.getCode())) {
            log.info("------------------- 小程序Api对接 - code换取用户手机号成功 ----------------------------");
            return Result.ok();
        } else {
            if (object.getString(AppletParamEnum.KEY_ERRMSG.getCode()).equals(AppletParamEnum.KEY_OK.getCode())) {
                log.info("------------------- 小程序Api对接 - code换取用户手机号成功 ----------------------------");
                return Result.ok();
            } else {
                log.info("------------------- 小程序Api对接 ---------------------------- code换取用户手机号 - 错误信息 -> {}", object.getString(AppletParamEnum.KEY_ERRMSG.getCode()));
                return Result.error("code换取用户手机号失败");
            }
        }
    }

    /**
     * 订阅消息 - 发送
     * @param accessToken
     * @param wxMssVo
     * @return
     */
    @Override
    public Result<JSONObject> sendNewTemplate(String accessToken, WxMssVo wxMssVo) {

        String requestUrl = appletProperties.getSEND_MESSAGE() + "?" + AppletParamEnum.KEY_ACCESS_TOKEN.getCode() + "=" + accessToken;

        // 校验环境
        if (StringUtils.isBlank(wxMssVo.getMiniprogram_state())){
            if (env.getProperty("spring.profiles.active").equals(AppletParamEnum.ENV_DEV.getCode())){
                wxMssVo.setMiniprogram_state(AppletParamEnum.MINIPROGRAM_STATE_DEVELOPER.getCode());
            }else if (env.getProperty("spring.profiles.active").equals(AppletParamEnum.ENV_TEST.getCode())){
                wxMssVo.setMiniprogram_state(AppletParamEnum.MINIPROGRAM_STATE_TRIAL.getCode());
            }else if (env.getProperty("spring.profiles.active").equals(AppletParamEnum.ENV_PROD.getCode())){
                wxMssVo.setMiniprogram_state(AppletParamEnum.MINIPROGRAM_STATE_FORMAL.getCode());
            }else {
                wxMssVo.setMiniprogram_state(AppletParamEnum.MINIPROGRAM_STATE_DEVELOPER.getCode());
            }
        }

        // 语言，默认简体中文
        if (StringUtils.isBlank(wxMssVo.getLang())){
            wxMssVo.setLang(AppletParamEnum.LANG_ZH_CN.getCode());
        }

        JSONObject bodyJO =
                httpClientUtils.postEntityByParam(requestUrl, wxMssVo);

        // 请求成功
        if (!bodyJO.containsKey(AppletParamEnum.KEY_ERRCODE.getCode())) {
            log.info("------------------- 小程序Api对接 - 订阅消息 - 发送 ----------------------------");
            return Result.ok();
        } else {
            if (bodyJO.getString(AppletParamEnum.KEY_ERRMSG.getCode()).equals(AppletParamEnum.KEY_OK.getCode())) {
                log.info("------------------- 小程序Api对接 - 订阅消息 - 发送 ----------------------------");
                return Result.ok();
            } else {
                log.info("------------------- 小程序Api对接 ---------------------------- 订阅消息 - 发送 - 错误信息 -> {}", bodyJO.getString(AppletParamEnum.KEY_ERRMSG.getCode()));
                return Result.error("订阅消息发送错误");
            }
        }

    }

}
