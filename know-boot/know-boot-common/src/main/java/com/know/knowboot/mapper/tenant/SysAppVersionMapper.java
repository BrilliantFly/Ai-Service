package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysAppVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * App热更新版本 Mapper
 */
@Mapper
public interface SysAppVersionMapper extends IBaseMapper<SysAppVersion> {

    /**
     * 查询比当前版本更新的最新已发布版本
     *
     * @param platform    平台 [android:安卓, ios:苹果]
     * @param versionCode 客户端当前版本号(内部比较用)
     * @return 最新版本列表(最多一条，无更新时返回空集合)
     */
    @Select("SELECT * FROM sys_app_version WHERE platform = #{platform} AND status = 1 AND (deleted = 0 OR deleted IS NULL) AND version_code > #{versionCode} ORDER BY version_code DESC LIMIT 1")
    List<SysAppVersion> selectLatest(@Param("platform") String platform, @Param("versionCode") Integer versionCode);
}
