package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysLoginLog;
import com.know.knowboot.entity.tenant.SysOperLog;

import java.util.List;

/**
 * 日志 Service 接口
 */
public interface ISysLogService extends IService<SysOperLog> {

    /**
     * 分页查询操作日志
     */
    IPage<SysOperLog> pageOperLog(SysOperLog query, Integer pageNum, Integer pageSize);

    /**
     * 获取操作日志列表
     */
    List<SysOperLog> listOperLog();

    /**
     * 删除操作日志
     */
    boolean deleteOperLog(Long id);

    /**
     * 清空操作日志
     */
    boolean clearOperLog();

    /**
     * 分页查询登录日志
     */
    IPage<SysLoginLog> pageLoginLog(SysLoginLog query, Integer pageNum, Integer pageSize);

    /**
     * 获取登录日志列表
     */
    List<SysLoginLog> listLoginLog();

    /**
     * 删除登录日志
     */
    boolean deleteLoginLog(Long id);

    /**
     * 清空登录日志
     */
    boolean clearLoginLog();
}