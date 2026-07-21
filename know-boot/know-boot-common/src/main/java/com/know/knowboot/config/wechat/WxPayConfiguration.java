package com.know.knowboot.config.wechat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

import com.know.knowboot.entity.Config;
import com.know.knowboot.entity.setting.DevPayConfig;
import com.know.knowboot.enums.PaymentEnum;
import com.know.knowboot.mapper.ConfigMapper;
import com.know.knowboot.mapper.setting.DevPayConfigMapper;
import com.know.knowboot.util.MapUtils;
import com.know.knowboot.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@ConditionalOnClass(WxPayService.class)
@AllArgsConstructor
public class WxPayConfiguration {

    @Resource
    DevPayConfigMapper devPayConfigMapper;

    @Resource
    ConfigMapper systemConfigMapper;

    /**
     * 微信小程序支付配置
     *
     * @author fzr
     * @return WxPayService
     */
    @Bean
    @ConditionalOnMissingBean
    public WxPayService mnpPayService() {
        DevPayConfig config = devPayConfigMapper.selectOne(
                new QueryWrapper<DevPayConfig>()
                    .eq("pay_way", PaymentEnum.WX_PAY.getCode())
                    .last("limit 1"));

        Config systemConfig = systemConfigMapper.selectOne(new QueryWrapper<Config>()
                .eq("type", "mnp_setting")
                .eq("name", "app_id")
                .last("limit 1"));

        String paramJson = (config == null || StringUtils.isNull(config.getConfig())) ? "{}" : config.getConfig().toString();
        Map<String, String> params = MapUtils.jsonToMap(paramJson);
        String appId = StringUtils.isNull(systemConfig) ? "" : systemConfig.getValue();
        String mchId = params.getOrDefault("mch_id", "");
        String paySignKey  = params.getOrDefault("pay_sign_key", "");
        byte[] privateKey  = params.getOrDefault("private_key", "").getBytes();
        byte[] privateCert = params.getOrDefault("private_cert", "").getBytes();

        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setApiV3Key(paySignKey);
        payConfig.setPrivateKeyContent(privateKey);
        payConfig.setPrivateCertContent(privateCert);
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 微信公众号支付配置
     *
     * @author fzr
     * @return WxPayService
     */
    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxOaService() {
        DevPayConfig config = devPayConfigMapper.selectOne(
                new QueryWrapper<DevPayConfig>()
                        .eq("pay_way", PaymentEnum.WX_PAY.getCode())
                        .last("limit 1"));

        Config systemConfig = systemConfigMapper.selectOne(new QueryWrapper<Config>()
                .eq("name", "app_id")
                .eq("type", "oa_setting")
                .last("limit 1"));

        String paramJson = (config == null || StringUtils.isNull(config.getConfig())) ? "{}" : config.getConfig().toString();
        Map<String, String> params = MapUtils.jsonToMap(paramJson);
        String appId = StringUtils.isNull(systemConfig) ? "" : systemConfig.getValue();
        String mchId = params.getOrDefault("mch_id", "");
        String paySignKey  = params.getOrDefault("pay_sign_key", "");
        byte[] privateKey  = params.getOrDefault("private_key", "").getBytes();
        byte[] privateCert = params.getOrDefault("private_cert", "").getBytes();

        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(mchId);
        payConfig.setAppId(appId);
        payConfig.setApiV3Key(paySignKey);
        payConfig.setPrivateKeyContent(privateKey);
        payConfig.setPrivateCertContent(privateCert);
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
