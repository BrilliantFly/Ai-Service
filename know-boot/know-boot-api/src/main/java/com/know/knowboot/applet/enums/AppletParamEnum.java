package com.know.knowboot.applet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppletParamEnum {

    KEY_APPID_ID("appid", "小程序唯一凭证，即AppID"),
    KEY_SECRET("secret", "小程序唯一凭证密钥，即AppSecret"),
    KEY_GRANT_TYPE("grant_type", "授权类型"),
    KEY_JS_CODE("js_code", "登录时获取的 code"),
    KEY_ACCESS_TOKEN("access_token", "接口调用凭证"),
    KEY_ID("id ", "模板标题id，可通过接口获取，也可登录小程序后台查看获取"),
    KEY_KEYWORD_ID_LIST("keyword_id_list", "模板标题id，可通过接口获取，也可登录小程序后台查看获取"),
    KEY_TEMPLATE_ID("template_id", "要删除的模板id要删除的模板id"),
    KEY_TOUSER("touser", "接收者（用户）的 openid"),
    KEY_PAGE("page", "点击模板卡片后的跳转页面"),
    KEY_FORM_ID("form_id", "表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id"),
    KEY_DATA("data", "模板内容，不填则下发空模板"),
    KEY_EMPHASIS_KEYWORD("emphasis_keyword", "模板需要放大的关键词，不填则默认无放大"),
    KEY_TID("tid", "模板标题 id，可通过接口获取，也可登录小程序后台查看获取"),
    KEY_KIDLIST("kidList", "开发者自行组合好的模板关键词列表"),
    KEY_SCENEDESC("sceneDesc", "服务场景描述"),
    KEY_PRITMPLID("priTmplId", "要删除的模板id"),
    KEY_MINIPROGRAM_STATE("miniprogram_state", "跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版"),
    KEY_LANG("lang", "进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN"),
    KEY_IDS("ids", "类目 id，多个用逗号隔开"),
    KEY_START("ids", "用于分页，表示从 start 开始。从 0 开始计数"),
    KEY_LIMIT("limit", "用于分页，表示拉取 limit 条记录"),
    KEY_CODE("code", "手机号获取凭证"),
    KEY_WEAPP_TEMPLATE_MSG("weapp_template_msg", "小程序模板消息相关的信息，可以参考小程序模板消息接口; 有此节点则优先发送小程序模板消息；（小程序模板消息已下线，不用传此节点）"),
    KEY_MP_TEMPLATE_MSG("mp_template_msg", "公众号模板消息相关的信息，可以参考公众号模板消息接口；有此节点并且没有weapp_template_msg节点时，发送公众号模板消息"),
    KEY_UNIONID("unionid", "为私密消息创建activity_id时，指定分享者为 unionid 用户。其余用户不能用此activity_id分享私密消息。openid与 unionid 填一个即可。私密消息暂不支持云函数生成activity id。"),
    KEY_OPENID("openid", "为私密消息创建activity_id时，指定分享者为 unionid 用户。其余用户不能用此activity_id分享私密消息。openid与 unionid 填一个即可。私密消息暂不支持云函数生成activity id。"),
    KEY_ACTIVITY_ID("activity_id", "动态消息的 ID，通过 updatableMessage.createActivityId 接口获取"),
    KEY_TARGET_STATE("target_state", "动态消息修改后的状态"),
    KEY_TEMPLATE_INFO("template_info", "动态消息对应的模板信息"),

    KEY_ERRCODE("errcode", "错误码"),
    KEY_ERRMSG("errmsg", "错误信息"),
    KEY_OK("ok", "成功"),

    VALUE_AUTHORIZATION_CODE_LOGIN("authorization_code", "授权类型"),
    VALUE_AUTHORIZATION_CODE_ACCESS_TOKEN("client_credential", "授权类型"),

    /**
     * 小程序消息推送消息类型
     */
    MINIPROGRAM_STATE_DEVELOPER("developer", "跳转小程序类型-开发版"),
    MINIPROGRAM_STATE_TRIAL("trial", "跳转小程序类型-体验版"),
    MINIPROGRAM_STATE_FORMAL("formal", "跳转小程序类型-正式版"),
    LANG_ZH_CN("zh_CN", "简体中文"),
    LANG_EN_US("en_US", "英文"),
    LANG_ZH_HK("zh_HK", "繁体中文"),
    LANG_ZH_TW("zh_TW", "繁体中文"),
    ENV_DEV("dev", "环境-开发环境"),
    ENV_TEST("test", "环境-测试环境"),
    ENV_PROD("prod", "环境-生成环境"),

    /**
     * 授权登录
     */
    KEY_AUTH("auth", "登录认证是否通过"),
    KEY_AUTH_MSG("msg", "登录认证是否通过"),
    KEY_TOKEN("token", "Token"),
    KEY_AUTH_OPENID("openid", "openid"),

    /**
     * 小程序消息模板
     */
    TEMPLATE_ID_TEST("ostRdFiLgO_hUQc-L8wnyg3c1uF1JaIhvnFUnbZ3pzE", "获取推送信息 - 测试模板"),
    TEMPLATE_ID_LEAVE("gwopzKRzlP8iq5j-_dPxCpfcOPZiP0Trx8Vdl_yhLvY", "获取推送信息 - 请假模板"),
    TEMPLATE_ID_INVOICE("gwopzKRzlP8iq5j-_dPxCi-SV_7oTvwlwNNk1c89s4Q", "获取推送信息 - 开票申请模板"),
    TEMPLATE_ID_SEAL("gwopzKRzlP8iq5j-_dPxCmuYLcHJFrUQVCVKvtnZA1Y", "获取推送信息 - 用章申请模板"),
    TEMPLATE_ID_CONTRACT("gwopzKRzlP8iq5j-_dPxCl0mFBg0535aRwwps7wapD0", "获取推送信息 - 合同申请模板"),
    TEMPLATE_ID_COMMON("gwopzKRzlP8iq5j-_dPxCsryDHSF1_wAWAWrP1WP7nQ", "获取推送信息 - 公共模板"),

    KEY_REQUEST_TOKEN("token", "Token"),

    ;

    private final String code;
    private final String name;



}
