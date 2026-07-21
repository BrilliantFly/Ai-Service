package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.tenant.SysTabbar;

import java.util.List;

public interface ISysTabbarService {

    IPage<SysTabbar> page(SysTabbar query, Integer pageNum, Integer pageSize);

    List<SysTabbar> listAll();

    boolean add(SysTabbar tabbar);

    boolean update(SysTabbar tabbar);

    boolean delete(Long id);

    SysTabbar getById(Long id);
}