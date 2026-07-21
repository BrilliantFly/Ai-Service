package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.tenant.SysDictType;

import java.util.List;

public interface ISysDictTypeService {

    IPage<SysDictType> page(SysDictType query, Integer pageNum, Integer pageSize);

    List<SysDictType> listAll();

    boolean add(SysDictType dictType);

    boolean update(SysDictType dictType);

    boolean delete(Long id);

    SysDictType getById(Long id);
}