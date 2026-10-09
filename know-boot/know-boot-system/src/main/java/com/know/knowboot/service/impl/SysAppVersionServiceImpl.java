package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysAppVersion;
import com.know.knowboot.mapper.tenant.SysAppVersionMapper;
import com.know.knowboot.service.ISysAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App热更新版本服务实现
 */
@Service
public class SysAppVersionServiceImpl extends ServiceImpl<SysAppVersionMapper, SysAppVersion> implements ISysAppVersionService {

    /**
     * wgt包下载服务器前缀
     */
    private static final String DOWNLOAD_PREFIX = "http://101.37.83.88:9191";

    @Autowired
    private SysAppVersionMapper sysAppVersionMapper;

    @Override
    public Map<String, Object> checkLatest(String platform, Integer versionCode) {
        Map<String, Object> result = new HashMap<>();
        // 客户端未传版本号时按0处理，即从最低版本开始比较
        Integer currentCode = versionCode != null ? versionCode : 0;
        List<SysAppVersion> latestList = sysAppVersionMapper.selectLatest(platform, currentCode);
        SysAppVersion latest = (latestList != null && !latestList.isEmpty()) ? latestList.get(0) : null;
        if (latest == null) {
            // 没有比当前版本更新的已发布版本，客户端无需更新
            result.put("hasUpdate", false);
            result.put("forceUpdate", false);
            result.put("latest", null);
            return result;
        }
        Map<String, Object> latestMap = new HashMap<>();
        latestMap.put("versionCode", latest.getVersionCode());
        latestMap.put("versionName", latest.getVersionName());
        // 相对路径拼接服务器前缀，形成可直接下载的完整地址
        latestMap.put("downloadUrl", DOWNLOAD_PREFIX + latest.getDownloadUrl());
        latestMap.put("fileSize", latest.getFileSize());
        latestMap.put("md5", latest.getMd5());
        latestMap.put("updateDesc", latest.getUpdateDesc());
        result.put("hasUpdate", true);
        result.put("forceUpdate", latest.getForceUpdate() != null && latest.getForceUpdate() == 1);
        result.put("latest", latestMap);
        return result;
    }
}
