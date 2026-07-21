package com.know.knowboot.applet.controller;

import com.alibaba.fastjson.JSONObject;
import com.know.knowboot.applet.dto.WxMsgDTO;
import com.know.knowboot.applet.service.AppletApiService;
import com.know.knowboot.applet.utils.WxMsgUtils;
import com.know.knowboot.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 小程序Api对接
 */
@Api(tags = "小程序Api对接")
@Slf4j
@RestController
@RequestMapping("/appletApi")
public class AppletApiController {

    @Autowired
    private AppletApiService appletApiService;

    @Autowired
    private WxMsgUtils wxMsgUtils;

    /**
     * 获取openId
     * 通过code获取openid，校验openid是否存在或绑定，绑定，就登录成功，没有绑定即绑定（手机号绑定授权登录）
     * @param code
     * @return
     */
    @ApiOperation("授权登录 - 获取openid")
    @GetMapping("/wxLogin")
    public Result<JSONObject> wxLogin(@RequestParam("code") String code) {

        if (StringUtils.isBlank(code)) {
            log.info("----------------------- 手机号授权登录 --------------------------- code为空");
            return Result.error("code为空");
        }
        Result<JSONObject> objectR = appletApiService.userLogin(code);
        if (objectR.isSuccess()) {
            JSONObject jsonObject = objectR.getResult();
            String openId = jsonObject.getString("openid");
            if (StringUtils.isBlank(openId)) {
                log.info("----------------------- 用户授权登录 --------------------------- opendid不存在");
                return Result.error("opendid不存在");
            }else {
                return Result.ok(openId);
            }
        }else {
            log.info("----------------------- 用户授权登录 --------------------------- 登录失败");
            return objectR;
        }
    }

    /**
     * 获取手机号
     * 通过openid、code获取手机号，然后手机号匹配数据库用户信息绑定，进行登录
     * @param code
     * @param openId
     * @return
     */
    @ApiOperation("手机号授权登录")
    @GetMapping("/phoneLogin")
    public Result<JSONObject> phoneLogin(@RequestParam("code") String code, @RequestParam("openId") String openId) {

        if (StringUtils.isBlank(code)) {
            log.info("----------------------- 手机号授权登录 --------------------------- code为空");
            return Result.error("code为空");
        }
        if (StringUtils.isBlank(openId)) {
            log.info("----------------------- 手机号授权登录 --------------------------- openId为空");
            return Result.error("openId为空");
        }

        Result<JSONObject> tokenObject = appletApiService.getAccessToken();
        if (tokenObject.isSuccess()) {
            JSONObject token = tokenObject.getResult();
            Result<JSONObject> objectR = appletApiService.getPhoneNumber(token.getString("access_token"), code);
            if (objectR.isSuccess()) {
                JSONObject jsonObject = objectR.getResult();
                JSONObject json = (JSONObject) jsonObject.get("phone_info");
                String phoneNumber = json.getString("phoneNumber");
                if (StringUtils.isBlank(phoneNumber)) {
                    log.info("----------------------- 手机号授权登录 --------------------------- phoneNumber不存在");
                    return Result.error("phoneNumber不存在");
                }else {
                    return Result.ok(phoneNumber);
                }
            } else {
                return objectR;
            }
        }else {
            return tokenObject;
        }
    }

    @ApiOperation("用户登录")
    @GetMapping("/userLogin")
    public Result<JSONObject> userLogin(@RequestParam("code") String code) {

        Result<JSONObject> result = appletApiService.userLogin(code);

        return result;

    }

    @ApiOperation("获取AccessToken")
    @GetMapping("/getAccessToken")
    public Result<JSONObject> getAccessToken() {

        Result<JSONObject> result = appletApiService.getAccessToken();

        return result;

    }

    @ApiOperation("code换取用户手机号")
    @GetMapping("/getPhoneNumber")
    public Result<JSONObject> getPhoneNumber(@RequestParam("code") String code) {

        Result<JSONObject> result = appletApiService.getPhoneNumber("63_108Xl_wjpwdYaCKrpI97egxAa4LHEa4RHhrfXQ5CULpXpcGeOs0Dinm46efPEv0JSvurm4fwqmwX7kVl4CiGRyDtBhkWFlMk2FVuMjeaagWapmBC-Y1E1XPv6-kGTCcAEAOBK", code);
        return result;

    }

    @ApiOperation("订阅消息 - 推送信息（测试推送接口）")
    @GetMapping("/sendNewTemplate")
    public JSONObject sendNewTemplate(HttpServletRequest httpServletRequest, @RequestBody WxMsgDTO wxMsgDTO) {

        wxMsgDTO.setBusinessType(null);
        wxMsgDTO.setApplicant("申请人");
        wxMsgDTO.setRemark("合同用章");
        wxMsgUtils.sendCommonMsg(httpServletRequest, wxMsgDTO);

        return null;

    }

}

