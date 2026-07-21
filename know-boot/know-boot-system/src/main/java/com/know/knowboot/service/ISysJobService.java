package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.tenant.SysJob;

import java.util.List;

/**
 * 岗位服务接口
 */
public interface ISysJobService {

    /**
     * 分页查询
     */
    IPage<SysJob> page(SysJob query, Integer pageNum, Integer pageSize);

    /**
     * 获取所有
     */
    List<SysJob> listAll();

    /**
     * 新增
     */
    boolean add(SysJob job);

    /**
     * 修改
     */
    boolean update(SysJob job);

    /**
     * 删除
     */
    boolean delete(Long id);

    SysJob getById(Long id);
}