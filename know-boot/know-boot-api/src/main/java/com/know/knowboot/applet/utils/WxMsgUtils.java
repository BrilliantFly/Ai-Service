package com.know.knowboot.applet.utils;

import com.know.knowboot.applet.enums.AppletParamEnum;
import com.know.knowboot.applet.service.AppletApiService;
import com.know.knowboot.applet.vo.TemplateDataVo;
import com.know.knowboot.applet.vo.WxMssVo;
import com.know.knowboot.applet.dto.WxMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信推送信息模板信息
 */
@Slf4j
@Component
public class WxMsgUtils {

    @Autowired
    private AppletApiService appletApiService;

//    @Autowired
//    private BaseUserService baseUserService;

    /**
     * 推送公共审批消息 - 调用节点：发起申请、审批
     *
     * @param httpServletRequest
     * @return
     */
//    @Async(value = AsyncConstant.SYSTEM_ASYNC_EXECUTOR)
    public void sendCommonMsg(HttpServletRequest httpServletRequest, WxMsgDTO wxMsgDTO) {
        String token = httpServletRequest.getHeader(AppletParamEnum.KEY_REQUEST_TOKEN.getCode());

//        // 用户
//        if (StringUtils.isNotBlank(wxMsgDTO.getUserIds())) {
//            List<BaseUser> userList = baseUserService.listByIds(Arrays.asList(wxMsgDTO.getUserIds().split(",")));
//            if (CollectionUtils.isNotEmpty(userList)) {
//                userList.forEach(baseUserVO -> {
//                    if (StringUtils.isNotBlank(baseUserVO.getOpendId())) {
//
//                        WxMssVo wxMssVo = getCommonSendMsg(baseUserVO.getOpendId(),
//                                AppletBusinessTypeEnum.getBusinessTypeByCode(wxMsgDTO.getBusinessType()).getUrl(),
//                                wxMsgDTO.getApplicant(),
//                                AppletBusinessTypeEnum.getBusinessTypeByCode(wxMsgDTO.getBusinessType()).getName(), wxMsgDTO.getRemark());
//                        R<JSONObject> objectR = appletApiService.sendNewTemplate(StringRedisUtils.get(WxTokenUtils.getTokenKey(token)), wxMssVo);
//                        if (!objectR.isSuccessFul()) {
//                            log.error("--------------- 推送公共审批消息 - 失败 ---------------- 接收人:{},失败原因:{}", baseUserVO.getUsername(), objectR.getData());
//                        }
//
//                    }
//                });
//            }
//
//            // 角色
//        } else if (StringUtils.isNotBlank(wxMsgDTO.getRoleId())) {
//            List<BaseUserVO> voList = baseUserService.getUserInfoByRoleId(wxMsgDTO);
//            if (CollectionUtils.isNotEmpty(voList)) {
//                voList.forEach(baseUserVO -> {
//                    if (StringUtils.isNotBlank(baseUserVO.getOpendId())) {
//
//                        WxMssVo wxMssVo = getCommonSendMsg(baseUserVO.getOpendId(),
//                                AppletBusinessTypeEnum.getBusinessTypeByCode(wxMsgDTO.getBusinessType()).getUrl(),
//                                wxMsgDTO.getApplicant(),
//                                AppletBusinessTypeEnum.getBusinessTypeByCode(wxMsgDTO.getBusinessType()).getName(), wxMsgDTO.getRemark());
//                        R<JSONObject> objectR = appletApiService.sendNewTemplate(StringRedisUtils.get(WxTokenUtils.getTokenKey(token)), wxMssVo);
//                        if (!objectR.isSuccessFul()) {
//                            log.error("--------------- 推送公共审批消息 - 失败 ---------------- 接收人:{},失败原因:{}", baseUserVO.getUsername(), objectR.getData());
//                        }
//                    }
//                });
//            }
//        }

    }

    /***
     * 获取推送信息 - 公共推送模板
     * @param touser 接收者-openid
     * @param page 击模板卡片后的跳转页面
     * @param applicant 申请人
     * @param type 业务类型：比如请假申请、用章申请
     * @param remark 备注
     * @return
     */
    public static WxMssVo getCommonSendMsg(String touser, String page, String applicant, String type, String remark) {

        Map<String, TemplateDataVo> dataVoMap = new HashMap<>();

        TemplateDataVo dataVo1 = new TemplateDataVo();
        dataVo1.setValue(applicant);
        dataVoMap.put("name1", dataVo1);

        TemplateDataVo dataVo2 = new TemplateDataVo();
        dataVo2.setValue(type);
        dataVoMap.put("thing17", dataVo2);

        TemplateDataVo dataVo3 = new TemplateDataVo();
        dataVo3.setValue(remark);
        dataVoMap.put("thing11", dataVo3);

        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(touser);
        wxMssVo.setTemplate_id(AppletParamEnum.TEMPLATE_ID_COMMON.getCode());
        wxMssVo.setPage(page);
        wxMssVo.setData(dataVoMap);

        return wxMssVo;
    }
}
