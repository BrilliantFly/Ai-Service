package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysDictType;
import com.know.knowboot.mapper.tenant.SysDictTypeMapper;
import com.know.knowboot.service.ISysDictTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Override
    public IPage<SysDictType> page(SysDictType query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getDictName() != null, SysDictType::getDictName, query.getDictName())
                .eq(query.getStatus() != null, SysDictType::getStatus, query.getStatus())
                .orderByAsc(SysDictType::getId);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysDictType> listAll() {
        return list(new LambdaQueryWrapper<SysDictType>().orderByAsc(SysDictType::getId));
    }

    @Override
    public boolean add(SysDictType dictType) {
        return save(dictType);
    }

    @Override
    public boolean update(SysDictType dictType) {
        return updateById(dictType);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public SysDictType getById(Long id) {
        return baseMapper.selectById(id);
    }
}