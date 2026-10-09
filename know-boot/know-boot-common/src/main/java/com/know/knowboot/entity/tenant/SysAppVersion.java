package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * App热更新版本实体
 */
@Data
@TableName("sys_app_version")
public class SysAppVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 平台 [android:安卓, ios:苹果]
     */
    private String platform;

    /**
     * 版本号(内部比较用，数值越大版本越新)
     */
    private Integer versionCode;

    /**
     * 版本名称(展示用，如 1.0.1)
     */
    private String versionName;

    /**
     * 下载地址(相对路径，如 /upgrade/wgt/xxx.wgt)
     */
    private String downloadUrl;

    /**
     * wgt包文件名
     */
    private String fileName;

    /**
     * wgt包大小(字节)
     */
    private Long fileSize;

    /**
     * wgt包MD5校验值
     */
    private String md5;

    /**
     * 是否强制更新 [0:可选更新, 1:强制更新]
     */
    private Integer forceUpdate;

    /**
     * 更新说明
     */
    private String updateDesc;

    /**
     * 状态 [0:已下架, 1:已发布]
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志 [0:正常, 1:已删除]
     */
    @TableLogic
    private Integer deleted;
}
