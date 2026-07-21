package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysLoginLog;
import com.know.knowboot.entity.tenant.SysOperLog;
import com.know.knowboot.mapper.tenant.SysLoginLogMapper;
import com.know.knowboot.mapper.tenant.SysOperLogMapper;
import com.know.knowboot.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志服务实现
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    public IPage<SysOperLog> pageOperLog(SysOperLog query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getTitle() != null, SysOperLog::getTitle, query.getTitle())
                .like(query.getOperName() != null, SysOperLog::getOperName, query.getOperName())
                .eq(query.getBusinessType() != null, SysOperLog::getBusinessType, query.getBusinessType())
                .eq(query.getStatus() != null, SysOperLog::getStatus, query.getStatus())
                .orderByDesc(SysOperLog::getOperTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysOperLog> listOperLog() {
        return list(new LambdaQueryWrapper<SysOperLog>()
                .orderByDesc(SysOperLog::getOperTime)
                .last("limit 500"));
    }

    @Override
    public boolean deleteOperLog(Long id) {
        return removeById(id);
    }

    @Override
    public boolean clearOperLog() {
        return sysOperLogMapper.delete(null) > 0;
    }

    @Override
    public IPage<SysLoginLog> pageLoginLog(SysLoginLog query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getUsername() != null, SysLoginLog::getUsername, query.getUsername())
                .eq(query.getStatus() != null, SysLoginLog::getStatus, query.getStatus())
                .orderByDesc(SysLoginLog::getLoginTime);
        return sysLoginLogMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysLoginLog> listLoginLog() {
        return sysLoginLogMapper.selectList(new LambdaQueryWrapper<SysLoginLog>()
                .orderByDesc(SysLoginLog::getLoginTime)
                .last("limit 500"));
    }

    @Override
    public boolean deleteLoginLog(Long id) {
        return sysLoginLogMapper.deleteById(id) > 0;
    }

    @Override
    public boolean clearLoginLog() {
        return sysLoginLogMapper.delete(null) > 0;
    }
}