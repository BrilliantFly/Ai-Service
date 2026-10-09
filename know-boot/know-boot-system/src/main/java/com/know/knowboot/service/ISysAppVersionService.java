package com.know.knowboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysAppVersion;

import java.util.Map;

/**
 * App热更新版本 Service 接口
 */
public interface ISysAppVersionService extends IService<SysAppVersion> {

    /**
     * 检测App是否有可用的新版本
     *
     * @param platform    平台 [android:安卓, ios:苹果]
     * @param versionCode 客户端当前版本号(内部比较用)
     * @return 检测结果 {hasUpdate:是否有更新, forceUpdate:是否强制更新, latest:最新版本信息}
     */
    Map<String, Object> checkLatest(String platform, Integer versionCode);
}
