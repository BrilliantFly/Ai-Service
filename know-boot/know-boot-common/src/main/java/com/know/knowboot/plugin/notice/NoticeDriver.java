package com.know.knowboot.plugin.notice;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.know.knowboot.entity.notice.NoticeSetting;
import com.know.knowboot.exception.OperateException;
import com.know.knowboot.mapper.notice.NoticeSettingMapper;
import com.know.knowboot.plugin.notice.engine.SmsNoticeHandle;
import com.know.knowboot.plugin.notice.template.SmsTemplate;
import com.know.knowboot.plugin.notice.vo.NoticeSmsVo;
import com.know.knowboot.common.SpringUtils;
import com.know.knowboot.util.StringUtils;

/**
 * 通知驱动
 */
public class NoticeDriver {

    public static void handle(NoticeSmsVo noticeSmsVo) {
        // 场景模板
        NoticeSettingMapper noticeSettingMapper = SpringUtils.getBean(NoticeSettingMapper.class);
        NoticeSetting noticeSetting = noticeSettingMapper.selectOne(
                new QueryWrapper<NoticeSetting>()
                        .eq("scene_id", noticeSmsVo.getScene())
                        .last("limit 1"));

        if (StringUtils.isNull(noticeSetting)) {
            throw new OperateException("消息场景不存在!");
        }

        // 短信通知
        SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setName(noticeSetting.getSceneName());
        smsTemplate.setType(noticeSetting.getType());
        smsTemplate.setParams(noticeSetting.getSmsNotice());
        if (StringUtils.isNotNull(smsTemplate.getStatus()) && smsTemplate.getStatus().equals(1)) {
            (new SmsNoticeHandle()).send(noticeSmsVo, smsTemplate);
        }
    }

}
